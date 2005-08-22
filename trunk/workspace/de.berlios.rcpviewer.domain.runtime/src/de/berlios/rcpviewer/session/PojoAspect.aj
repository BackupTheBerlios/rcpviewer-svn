package de.berlios.rcpviewer.session;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import org.eclipse.emf.ecore.EAttribute;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.FieldSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject;

public abstract aspect PojoAspect {
	
	/**
	 * All pojos that are have an {@link de.berlios.rcpviewer.progmodel.standard.InDomain} 
	 * annotation should implement {@link de.berlios.rcpviewer.session.IPojo}. 
	 */
	declare parents: (@InDomain *) implements IPojo;

	/**
	 * Introduce implementation of {@link IPojo} to @InDomain pojos.
	 * 
	 * JAVA_5_FIXME
	 */
	public IDomainObject IPojo.getDomainObject() {
		InDomain inDomain = this.getClass().getAnnotation(InDomain.class);
		String domainName = inDomain.value();
		RuntimeDomain domain = RuntimeDomain.instance(domainName);
		SessionManager sessionManager = SessionManager.instance(); 
		for(ISession session: sessionManager.getAllSessions()) {
			if (domain != session.getDomain()) {
				continue;
			}
			if (!session.hasDomainObjectFor(this)) {
				continue;
			}
			return session.getDomainObjectFor(this, this.getClass());
		}
		return null;
	}

	/**
	 * used for percflow: an aspect is instantiated each time an operation
	 * is invoked on a pojo
	 * 
	 * <p>
	 * protected for sub-aspects
	 */
	protected pointcut invokeOperationOnPojo(IPojo pojo): 
		invokePublicMethodOnPojo(pojo) &&
		!invokeAccessorOnPojo(Object) &&
		!invokeMutatorOnPojo(Object, Object) &&
		!within(PojoAspect);
	
	/**
	 * protected for sub-aspects
	 */
	protected pointcut invokePublicMethodOnPojo(IPojo pojo): 
		execution(public * IPojo+.*(..)) && this(pojo);
	
	/**
	 * protected for sub-aspects
	 */
	protected pointcut invokeAccessorOnPojo(IPojo pojo): 
		execution(public !void IPojo+.get*()) && this(pojo);
	
	/**
	 * protected for sub-aspects
	 */
	protected pointcut invokeMutatorOnPojo(IPojo pojo, Object postValue): 
		execution(public void IPojo+.set*(*)) && this(pojo) && args(postValue);
	
	/**
	 * Capture an attribute being changed on some pojo.
	 * 
	 * <p>
	 * The pojo being changed could either be the one on which the operation
	 * has been invoked, or could be some other pojo entirely.
	 * 
	 * <p>
	 * protected for sub-aspects
	 * 
	 */
	protected pointcut changeAttributeOnPojo(IPojo pojo, Object postValue) :
		args(postValue) && this(pojo) && set(* IPojo+.*);


	/**
	 * Notice that an attribute has changed.
	 * 
	 * <p>
	 * Overload of the other version but not exposing the argument.
	 */
	protected pointcut modifyAttributeOnPojoNoArg(IPojo pojo) :
		this(pojo) && set(* IPojo+.*);

	

	/**
	 * Capture a pojo being instantiated.
	 */
	public pointcut instantiatePojo(IPojo pojo): 
		execution(IPojo+.new(..)) && this(pojo);

	
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
	 * @return attribute - guaranteed to be non-null (else an assertion error will be thrown).
	 */
	protected IDomainObject.IAttribute getAttributeFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		EAttribute eAttribute = getEAttributeFor(domainObject, joinPointStaticPart);
		IDomainObject.IAttribute attribute = domainObject.getAttribute(eAttribute);
		return attribute;
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
	 * @return attribute - guaranteed to be non-null (else an assertion error will be thrown).
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
