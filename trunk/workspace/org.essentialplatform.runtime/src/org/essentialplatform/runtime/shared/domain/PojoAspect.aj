package org.essentialplatform.runtime.shared.domain;

import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;

import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.DomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;

public abstract aspect PojoAspect {
	
	/**
	 * All pojos that are have an {@link org.essentialplatform.progmodel.standard.InDomain} 
	 * annotation should implement {@link org.essentialplatform.session.IPojo}. 
	 */
	declare parents: (@InDomain *) implements IPojo;

	private IDomainObject IPojo._domainObject;

	/**
	 * Lazily creates the {@link IDomainObject} wrapper for a given
	 * {@link IPojo}.
	 * 
	 * <p>
	 * The wrapper is created lazily because the field itself is not
	 * transmitted across the wire.
	 */
	public synchronized IDomainObject IPojo.domainObject() {
		if (_domainObject == null) {
			_domainObject = new DomainObject(this);
		}
		return _domainObject;
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
	 * Whether the pojo can be enlisted in a transaction.
	 * 
	 * <p>
	 * Has been moved up from (the now-defunct) TransactionAspect since isn't 
	 * really to do with transactions and saves us having to have a 
	 * transactionalXxx version of each of the pointcuts defined in PojoAspect. 
	 * 
	 * <p>
	 * We mustn't attempt to enlist a pojo that is being constructed.  The
	 * implementation of {@link DomainObject} is such we only set up its 
	 * persistence state and its resolve state at the end of its constructor.
	 * We will ignore objects whose state is not yet fully specified.
	 */
	protected static boolean canBeEnlisted(final IPojo pojo) {
		IDomainObject domainObject = pojo._domainObject;
		if (domainObject == null) {
			return false;
		}
		return domainObject.getResolveState() != null &&
		       !domainObject.getResolveState().isUnknown() &&
		       domainObject.getPersistState() != null &&
		       !domainObject.getPersistState().isUnknown();
	}



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
		execution(public void IPojo+.set*(*)) && 
		this(pojo) && 
		args(postValue);
	
	
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
		execution(public void IPojo+.set*(IPojo+)) && 
		this(pojo) && 
		args(newReferencedObjectOrNull);

	
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
	 * ANY ADVICE SHOULD APPEAR LEXICALLY BELOW THE invoke... POINTCUTS TO
	 * ENSURE LOWER PRECEDENCE. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 * 
	 */
	protected pointcut changingAttributeOnPojo(IPojo pojo, Object postValue) :
		set((!IPojo+ && !java.util.Collection+) IPojo+.*)  &&
		args(postValue) && 
		this(pojo) && 
		if(canBeEnlisted(pojo)); 

	

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
	 * ANY ADVICE SHOULD APPEAR LEXICALLY BELOW THE invoke... POINTCUTS TO
	 * ENSURE LOWER PRECEDENCE. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 * 
	 */
	protected pointcut changingOneToOneReferenceOnPojo(IPojo pojo, IPojo referencedObjectOrNull) :
		set(IPojo+ IPojo+.*) && 
		this(pojo) && 
		args(referencedObjectOrNull) &&
		if(canBeEnlisted(pojo));

	


	/**
	 * Capture a collection in a pojo has been added to.
	 * 
	 * <p>
	 * This is different from the invokeAddToCollectionOnPojo pointcut 
	 * because it fires however the collection is modified (directly or not).
	 * 
	 * <p>
	 * ANY ADVICE SHOULD APPEAR LEXICALLY BELOW THE invoke... POINTCUTS TO
	 * ENSURE LOWER PRECEDENCE. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut addingToCollectionOnPojo(IPojo pojo, java.util.Collection collection, Object addedObj): 
		call(public boolean java.util.Collection+.add(Object+)) && 
		args(addedObj) && 
		this(pojo)  && 
		target(collection)  && 
		!within(java.util..*)  &&
		if(canBeEnlisted(pojo));



	/**
	 * Capture a collection in a pojo has been removed from.
	 * 
	 * <p>
	 * This is different from the invokeRemoveFromCollectionOnPojo pointcut 
	 * because it fires however the collection is modified (directly or not).
	 * 
	 * <p>
	 * ANY ADVICE SHOULD APPEAR LEXICALLY BELOW THE invoke... POINTCUTS TO
	 * ENSURE LOWER PRECEDENCE. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut removingFromCollectionOnPojo(IPojo pojo, java.util.Collection collection, Object removedObj): 
		call(public boolean java.util.Collection+.remove(Object+)) && 
		args(removedObj) && 
		this(pojo) && 
		target(collection) && 
		!within(java.util..*) &&
		if(canBeEnlisted(pojo));


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
		execution(public void IPojo+.delete()) && 
		this(pojo) && 
		if(canBeEnlisted(pojo));

}
