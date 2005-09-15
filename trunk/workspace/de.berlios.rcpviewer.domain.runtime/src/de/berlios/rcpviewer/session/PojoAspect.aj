package de.berlios.rcpviewer.session;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.Collection;

import org.eclipse.emf.ecore.EAttribute;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.FieldSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.ConstructorSignature;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EOperation;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.progmodel.standard.DomainObject;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;


public abstract aspect PojoAspect {
	
	/**
	 * All pojos that are have an {@link de.berlios.rcpviewer.progmodel.standard.InDomain} 
	 * annotation should implement {@link de.berlios.rcpviewer.session.IPojo}. 
	 */
	declare parents: (@InDomain *) implements IPojo;
	private IDomainObject IPojo._domainObject = new DomainObject(this);

	/**
	 * Introduces the implementation of obtaining the {@link IDomainObject}
	 * that wraps this {@link IPojo} in some {@link ISession}.
	 * 
	 * <p>
	 * TODO: is this right, that we only have an IDomainObject wrapper when the
	 * pojo is attached to a session?
	 */
	public IDomainObject IPojo.getDomainObject() {
		return _domainObject;
//		InDomain inDomain = this.getClass().getAnnotation(InDomain.class);
//		String domainName = inDomain.value();
//		RuntimeDomain domain = RuntimeDomain.instance(domainName);
//		SessionManager sessionManager = SessionManager.instance(); 
//		for(ISession session: sessionManager.getAllSessions()) {
//			if (domain != session.getDomain()) {
//				continue;
//			}
//			if (!session.hasDomainObjectFor(this)) {
//				continue;
//			}
//			return session.getDomainObjectFor(this, this.getClass());
//		}
//		return null;
	}

	/**
	 * Captures the invocation of any operation on a pojo.
	 * 
	 * <p>
	 * Public methods which represent getters, setters, associators, 
	 * dissociators, addTo and removeFrom are excluded. 
	 * 
	 * <p>
	 * protected for sub-aspects
	 */
	protected pointcut invokeOperationOnPojo(IPojo pojo): 
		invokePublicMethodOnPojo(pojo) &&
		!invokeGetterForAttributeOnPojo(IPojo) &&
		!invokeSetterForAttributeOnPojo(IPojo, Object) &&
		!invokeGetterForOneToOneReferenceOnPojo(IPojo) &&
		!invokeSetterForOneToOneReferenceOnPojo(IPojo, IPojo) &&
		!invokeAssociatorForOneToOneReferenceOnPojo(IPojo, IPojo) &&
		!invokeDissociatorForOneToOneReferenceOnPojo(IPojo, IPojo) &&
		!invokeAddToCollectionOnPojo(IPojo, IPojo) &&
		!invokeRemoveFromCollectionOnPojo(IPojo, IPojo) &&
		!within(PojoAspect);
	
	/////////////////////////////////////////////////////////////////////
	
	/**
	 * Captures the invocation of any accessor (getter) of an attribute on a 
	 * pojo.
	 * 
	 * <p>
	 * Since this is a potential start of an interaction initiated from the UI, 
	 * it is useful in defining either the start of a transaction or the
	 * start of a new ChangeSet to add to an existing transaction. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut invokeGetterForAttributeOnPojo(IPojo pojo): 
		execution(public !void IPojo+.get*()) && this(pojo);
	
	/**
	 * Captures the invocation of any mutator (setter) of an attribute on a 
	 * pojo.
	 * 
	 * <p>
	 * Since this is a potential start of an interaction initiated from the UI, 
	 * it is useful in defining either the start of a transaction or the
	 * start of a new ChangeSet to add to an existing transaction. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut invokeSetterForAttributeOnPojo(IPojo pojo, Object postValue): 
		execution(public void IPojo+.set*(*)) && this(pojo) && args(postValue);
	
	/**
	 * Captures the invocation of any accessor (getter) of an 1:1 reference on a 
	 * pojo.
	 * 
	 * <p>
	 * Since this is a potential start of an interaction initiated from the UI, 
	 * it is useful in defining either the start of a transaction or the
	 * start of a new ChangeSet to add to an existing transaction. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut invokeGetterForOneToOneReferenceOnPojo(IPojo pojo): 
		execution(public IPojo+ IPojo+.get*()) && this(pojo);
	
	
	/**
	 * Captures the invocation of any mutator (setter) of an 1:1 reference on a 
	 * pojo.
	 * 
	 * <p>
	 * Since this is a potential start of an interaction initiated from the UI, 
	 * it is useful in defining either the start of a transaction or the
	 * start of a new ChangeSet to add to an existing transaction. 
	 * 
	 * <p>
	 * This is an alternative programming model to using associators and
	 * dissociators. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut invokeSetterForOneToOneReferenceOnPojo(IPojo pojo, IPojo newReferencedObjectOrNull): 
		execution(public void IPojo+.set*(IPojo+)) && this(pojo) && args(newReferencedObjectOrNull);

	/**
	 * Captures the invocation of any associator (associate method) of a 
	 * 1:1 reference on a pojo.
	 * 
	 * <p>
	 * Since this is a potential start of an interaction initiated from the UI, 
	 * it is useful in defining either the start of a transaction or the
	 * start of a new ChangeSet to add to an existing transaction. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut invokeAssociatorForOneToOneReferenceOnPojo(IPojo pojo, IPojo newReferencedObject): 
		execution(public void IPojo+.associate*(IPojo+)) && this(pojo) && args(newReferencedObject);
	
	/**
	 * Captures the invocation of any dissociator (dissociate method) of a 
	 * 1:1 reference on a pojo.
	 * 
	 * <p>
	 * Since this is a potential start of an interaction initiated from the UI, 
	 * it is useful in defining either the start of a transaction or the
	 * start of a new ChangeSet to add to an existing transaction. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut invokeDissociatorForOneToOneReferenceOnPojo(IPojo pojo, IPojo existingReferencedObject): 
		execution(public void IPojo+.dissociate*(IPojo+)) && this(pojo) && args(existingReferencedObject);
	
	/**
	 * Captures the invocation of any addTo method for adding objects to an
	 * collection on a pojo. 
	 * 
	 * <p>
	 * Since this is a potential start of an interaction initiated from the UI, 
	 * it is useful in defining either the start of a transaction or the
	 * start of a new ChangeSet to add to an existing transaction. 
	 * 
	 * <p>
	 * Note that any visibility will do; the NotifyListenersAspect uses this
	 * (parsing the method name...).
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut invokeAddToCollectionOnPojo(IPojo pojo, IPojo addedObj): 
		execution(void IPojo+.addTo*(IPojo+)) && this(pojo) && args(addedObj);
	
	/**
	 * Captures the invocation of any removeFrom method for removing objects 
	 * from a collection on a pojo. 
	 * 
	 * <p>
	 * Note that any visibility will do; the NotifyListenersAspect uses this
	 * (parsing the method name...).
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut invokeRemoveFromCollectionOnPojo(IPojo pojo, IPojo removedObj): 
		execution(void IPojo+.removeFrom*(IPojo+)) && this(pojo) && args(removedObj);
	
	/**
	 * Captures the invokation of any public method on a pojo, excluding 
	 * prerequisites methods and also excluding those methods inherited from 
	 * the Object class itself.
	 * 
	 * <p>
	 * Private because it doesn't repreesnt a semantic that sub-aspects should
	 * particularly be interested in.
	 */
	private pointcut invokePublicMethodOnPojo(IPojo pojo): 
		execution(public * IPojo+.*(..)) && 
		!execution(public IDomainObject IPojo+.getDomainObject()) && 
		!execution(public IPrerequisites IPojo+.*Pre(..)) && 
		!execution(public * Object.*()) && 
		this(pojo);
	

	/**
	 * Capture an attribute being changed on some pojo.
	 * 
	 * <p>
	 * This is different from the invokeSetterForAttributeOnPojo pointcut 
	 * because it fires however the attribute is modified (directly or not).
	 * One use is to allows changes to be aggregated, as part of a ChangeSet 
	 * of a transaction already under way.
	 * 
	 * <p>
	 * SHOULD APPEAR LEXICALLY BELOW THE invoke... POINTCUTS SINCE HAS LOWER
	 * PRECEDENCE. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 * 
	 */
	protected pointcut changingAttributeOnPojo(IPojo pojo, Object postValue) :
		args(postValue) && this(pojo) && set((!IPojo+ && !Collection+) IPojo+.*);


	/**
	 * Capture a 1:1 reference being changed on some pojo.
	 * 
	 * <p>
	 * This is different from the invoke{Setter/Associator/Dissociator}ForOneToOneReferenceOnPojo pointcuts 
	 * because it fires however the reference is modified (directly or not).
	 * One use is to allows changes to be aggregated, as part of a ChangeSet 
	 * of a transaction already under way.
	 * 
	 * <p>
	 * SHOULD APPEAR LEXICALLY BELOW THE invoke... POINTCUTS SINCE HAS LOWER
	 * PRECEDENCE. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 * 
	 */
	protected pointcut changingOneToOneReferenceOnPojo(IPojo pojo, IPojo referencedObjectOrNull) :
		set(IPojo+ IPojo+.*) && this(pojo) && args(referencedObjectOrNull);


	/**
	 * Capture a collection in a pojo has been added to.
	 * 
	 * <p>
	 * This is different from the invokeAddToCollectionOnPojo pointcut 
	 * because it fires however the collection is modified (directly or not).
	 * 
	 * <p>
	 * SHOULD APPEAR LEXICALLY BELOW THE invoke... POINTCUTS SINCE HAS LOWER
	 * PRECEDENCE. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut addingToCollectionOnPojo(IPojo pojo, Collection collection, Object addedObj): 
		call(public boolean java.util.Collection+.add(Object+)) && 
		args(addedObj) && 
		this(pojo)  && 
		target(collection)  && 
		!within(java.util..*) ;

	/**
	 * Capture a collection in a pojo has been removed from.
	 * 
	 * <p>
	 * This is different from the invokeRemoveFromCollectionOnPojo pointcut 
	 * because it fires however the collection is modified (directly or not).
	 * 
	 * <p>
	 * SHOULD APPEAR LEXICALLY BELOW THE invoke... POINTCUTS SINCE HAS LOWER
	 * PRECEDENCE. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut removingFromCollectionOnPojo(IPojo pojo, Collection collection, Object removedObj): 
		call(public boolean java.util.Collection+.remove(Object+)) && 
		args(removedObj) && 
		this(pojo) && 
		target(collection) && 
		!within(java.util..*) ;


	declare error: 
		call(public boolean java.util.Collection+.add(Object+)) &&
		withincode(* IPojo+.*(..)) &&
		!withincode(void IPojo+.addTo*(..)):
			"can only add to collection from within an addto method"; 

	declare error: 
		call(public boolean java.util.Collection+.remove(Object+)) &&
		withincode(* IPojo+.*(..)) &&
		!withincode(void IPojo+.removeFrom*(..)):
			"can remove from collection from within an addto method"; 

		// the idea here is to ensure we only do add or remove from collections
	// from within addTo or removeFrom methods.  However

//	private pointcut addingToCollectionOutsideAnAddToMethod(): 
//		call(public boolean java.util.Collection+.add(Object+)) &&
//		!cflowbelow(execution(void IPojo+.addTo*(IPojo+))); 
//		
//	private pointcut removingFromCollectionOutsideARemoveFromMethod(): 
//		call(public boolean java.util.Collection+.remove(Object+)) &&
//		!cflowbelow(execution(void IPojo+.removeFrom*(IPojo+)));

//	before(): addingToCollectionOutsideAnAddToMethod() {
//		throw new RuntimeException("Programmer error: cannot add to collection outside an addTo method");
//	}
//	before(): removingFromCollectionOutsideARemoveFromMethod() { 
//		throw new RuntimeException("Programmer error: cannot remove from collection outside a removeFrom method");
//	}

	/**
	 * Capture a pojo being instantiated.
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut instantiatingPojo(IPojo pojo): 
		execution(public IPojo+.new(..)) && this(pojo);

	
	/**
	 * Capture a pojo being deleted.
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut deletingPojoUsingDeleteMethod(IPojo pojo): 
		execution(public void IPojo+.delete()) && this(pojo);

	////////////////////////////////////////////////////////////////////
	
	/**
	 * Looks up the {@link Field} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	protected Field getFieldFor(JoinPoint.StaticPart joinPointStaticPart) {
		FieldSignature signature = (FieldSignature)joinPointStaticPart.getSignature();
		// workaround to force signature to be able to pick up fields (soft references)
		Field[] fields = signature.getDeclaringType().getFields();
		Field field = signature.getField();
		return field;
	}


	/**
	 * Looks up the {@link Method} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	protected Method getMethodFor(JoinPoint.StaticPart joinPointStaticPart) {
		MethodSignature signature = (MethodSignature)joinPointStaticPart.getSignature();
		// workaround to force signature to be able to pick up methods (soft references)
		Method[] methods = signature.getDeclaringType().getMethods();
		return signature.getMethod();
	}

	
	/**
	 * Looks up the {@link Constructor} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	protected Constructor getConstructorFor(JoinPoint.StaticPart joinPointStaticPart) {
		ConstructorSignature signature = (ConstructorSignature)joinPointStaticPart.getSignature();
		// workaround to force signature to be able to pick up constructors (soft references)
		Constructor[] constructors = signature.getDeclaringType().getConstructors();
		return signature.getConstructor();
	}

	
	/**
	 * Looks up the {@link EAttribute} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	protected EAttribute getEAttributeFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		Signature signature = joinPointStaticPart.getSignature();
		String name = signature.getName();
		EAttribute attribute = domainObject.getEAttributeNamed(name);
		return attribute;
	}


	/**
	 * Looks up the {@link EReference} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 * 
	 * @param domainObject
	 * @param joinPoint representing either an addTo or a removeFrom operation.
	 */
	protected EReference getEReferenceFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		Signature signature = joinPointStaticPart.getSignature();
		String name = signature.getName();
		if (name.startsWith("addTo")) {
			name = camelCase(name.substring("addTo".length()));
		} else if (name.startsWith("removeFrom")) {
			name = camelCase(name.substring("removeFrom".length()));
		}
		EReference reference = domainObject.getEReferenceNamed(name);
		return reference;
	}
	
	private String camelCase(final String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		return Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}


	/**
	 * Looks up the {@link EOperation} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	protected EOperation getEOperationFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		Signature signature = joinPointStaticPart.getSignature();
		String name = signature.getName();
		EOperation operation = domainObject.getEOperationNamed(name);
		return operation;
	}


	/**
	 * Looks up the {@link IDomainObject.IAttribute} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 * 
	 * @return attribute or null
	 */
	protected IDomainObject.IAttribute getAttributeFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		EAttribute eAttribute = getEAttributeFor(domainObject, joinPointStaticPart);
		IDomainObject.IAttribute attribute = domainObject.getAttribute(eAttribute);
		return attribute;
	}


	/**
	 * Looks up the {@link IDomainObject.IOneToOneReference} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 * 
	 * @return reference or null
	 */
	protected IDomainObject.IOneToOneReference getOneToOneReferenceFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		EReference eReference = getEReferenceFor(domainObject, joinPointStaticPart);
		IDomainObject.IOneToOneReference reference = domainObject.getOneToOneReference(eReference);
		return reference;
	}


	/**
	 * Looks up the {@link IDomainObject.ICollectionReference} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 * 
	 * @param domainObject
	 * @param joinPoint representing either an addTo or a removeFrom operation.
	 * @return reference or null
	 */
	protected IDomainObject.ICollectionReference getCollectionReferenceFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		EReference eReference = getEReferenceFor(domainObject, joinPointStaticPart);
		IDomainObject.ICollectionReference reference = domainObject.getCollectionReference(eReference);
		return reference;
	}

	
	/**
	 * Looks up the {@link IDomainObject.IAttribute} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	protected IDomainObject.IOperation getOperationFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		EOperation eOperation = getEOperationFor(domainObject, joinPointStaticPart);
		IDomainObject.IOperation operation = domainObject.getOperation(eOperation);
		return operation;
	}

	
	/**
	 * Looks up the {@link IExtendedDomainObject.IExtendedAttribute} that 
	 * corresponds to the signature represented by the supplied 
	 * {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 * 
	 * @return attribute or null
	 */
	protected IExtendedDomainObject.IExtendedAttribute getExtendedAttributeFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		// TODO: the cast could be avoided if IDomainObject were declared instead as IDomainObject<?>.
		// however, in AspectJ as of 20050816 this blows up the compiler
		IExtendedDomainObject extendedDomainObject = 
			(IExtendedDomainObject)domainObject.getAdapter(IExtendedDomainObject.class);
		EAttribute eAttribute = getEAttributeFor(domainObject, joinPointStaticPart);
		IExtendedDomainObject.IExtendedAttribute extendedAttribute = 
			extendedDomainObject.getAttribute(eAttribute);
		return extendedAttribute;
	}


	/**
	 * Looks up the {@link IExtendedDomainObject.IExtendedReference} that 
	 * corresponds to the signature represented by the supplied 
	 * {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 * 
	 * @return reference or null
	 */
	protected IExtendedDomainObject.IExtendedReference getExtendedReferenceFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		// TODO: the cast could be avoided if IDomainObject were declared instead as IDomainObject<?>.
		// however, in AspectJ as of 20050816 this blows up the compiler
		IExtendedDomainObject extendedDomainObject = 
			(IExtendedDomainObject)domainObject.getAdapter(IExtendedDomainObject.class);
		EReference eReference = getEReferenceFor(domainObject, joinPointStaticPart);
		IExtendedDomainObject.IExtendedReference extendedReference = 
			extendedDomainObject.getReference(eReference);
		return extendedReference;
	}


	/**
	 * Looks up the {@link IExtendedDomainObject.IExtendedOperation} that 
	 * corresponds to the signature represented by the supplied 
	 * {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	protected IExtendedDomainObject.IExtendedOperation getExtendedOperationFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		// TODO: the cast could be avoided if IDomainObject were declared instead as IDomainObject<?>.
		// however, in AspectJ as of 20050816 this blows up the compiler
		IExtendedDomainObject extendedDomainObject = 
			(IExtendedDomainObject)domainObject.getAdapter(IExtendedDomainObject.class);
		EOperation eOperation = getEOperationFor(domainObject, joinPointStaticPart);
		IExtendedDomainObject.IExtendedOperation extendedOperation = 
			extendedDomainObject.getOperation(eOperation);
		return extendedOperation;
	}
}
