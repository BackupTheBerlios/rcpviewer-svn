package org.essentialplatform.runtime.transaction.internal;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IObservedFeature;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.session.ISession;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.changes.AttributeChange;
import org.essentialplatform.runtime.transaction.changes.IChange;


/**
 * One change per modified attribute performed directly (ie not programmatically
 * from an invoked operation).
 */
public aspect TransactionAttributeChangeAspect extends TransactionAspect {

	private final static Logger LOG = Logger.getLogger(TransactionAttributeChangeAspect.class);
	protected Logger getLogger() { return LOG; }

	protected pointcut transactionalChange(IPojo pojo): 
		transactionalChangingAttributeOnPojo(IPojo, Object) && 
		this(pojo) && 
		if(canBeEnlisted(pojo)) &&
		!cflowbelow(invokeOperationOnPojo(IPojo)) ;  // this is probably unnecessary since the invokeOperation aspect has precedence over this one...

	/**
	 * Obtains transaction from either the thread or from the pojo (checking
	 * that they don't conflict).
	 * 
	 * <p>
	 * This code is identical in all subaspects of TransactionChange, however
	 * moving it up and declaring a precedence doesn't seem to do the trick.
	 */
	Object around(IPojo pojo): transactionalChange(pojo) {
		getLogger().debug("transactionalChange(pojo=" + pojo+"): start");
		ITransactable transactable = (ITransactable)pojo;
		boolean transactionOnThread = hasTransactionForThread();
		ITransaction transaction = currentTransaction(transactable);
		if (!transactionOnThread) {
			getLogger().debug("no xactn for thread, setting; xactn=" + transaction);
			setTransactionForThread(transaction);
		} else {
			getLogger().debug("(xactn for thread already present)");
		}
		boolean startedInteraction = transaction.startingInteraction();
		try {
			return proceed(pojo);
		} finally {
			if (startedInteraction) {
				transaction.completingInteraction();
			}
			if (!transactionOnThread) {
				getLogger().debug("clearing xactn on thread; xactn=" + transaction);
				clearTransactionForThread();
			}
			getLogger().debug("transactionalChange(pojo=" + pojo+"): end");
		}
	}

	/**
	 * Creates an AttributeChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * In addition, passes the IDomainObject's IAttribute to the change so that
	 * it can notify listeners as it is executed/undone.  
	 *  
	 * <p>
	 * The change also notifies all {@link IObservedFeature}s of the session.  
	 * That's because a prerequisite of an operation or an attribute might 
	 * become satisfied (or no longer satisfied) as a result of this change.
	 * 
	 * <p>
	 * <n>Implementation notes</n>: informing all observed features seems rather
	 * crude.  An alternative design and possibly preferable approach would be 
	 * to wait until the current "workgroup" (as defined by the transaction 
	 * aspect) has completed.
	 *    
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied. 
	 */
	Object around(IPojo pojo, Object postValue): 
			transactionalChangingAttributeOnPojo(pojo, postValue) {
		getLogger().debug("changingAttributeOnPojo(pojo=" + pojo+", postValue='" + postValue + "'): start");
		try {
			Field field = getFieldFor(thisJoinPointStaticPart);
			
			ITransactable transactable = (ITransactable)pojo;
			ITransaction transaction = currentTransaction(transactable);
			
			IDomainObject domainObject = pojo.getDomainObject();
			IDomainObject.IObjectAttribute attribute = null;
			if (domainObject.getPersistState() != PersistState.UNKNOWN) {
				attribute = getAttributeFor(domainObject, thisJoinPointStaticPart);
			}
			IChange change = new AttributeChange(transaction, transactable, field, postValue, attribute);
			
			Object retval = change.execute();
			
			return retval;
		} finally {
			getLogger().debug("changingAttributeOnPojo(pojo=" + pojo+", postValue='" + postValue + "'): end");
		}

	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////
	// TEMPORARILY MOVED FROM NOTIFYLISTENERS, SINCE FOR SOME REASON NOT BEING APPLIED...
	////////////////////////////////////////////////////////////////////////////////////////
	


	/**
	 * If we are able to locate the {@link org.essentialplatform.session.IDomainObject}
	 * wrapper for this pojo then get it to notify any listeners it has for
	 * this 1:1 reference.
	 * 
	 * <p>
	 * In addition, notify all {@link IObservedFeature}s of the session.  That's because
	 * a prerequisite of an operation or an attribute might become satisfied
	 * (or no longer satisfied) as a result of this change.
	 * 
	 * <p>
	 * <n>Implementation notes</n>: informing all observed features seems rather
	 * crude.  An alternative design and possibly preferable approach would be 
	 * to wait until the current "workgroup" (as defined by the transaction 
	 * aspect) has completed.   
	 */
	after(IPojo pojo, Object newReference): changingOneToOneReferenceOnPojo(pojo, newReference) { 
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject == null || domainObject.getPersistState() == PersistState.UNKNOWN) {
			return;
		}
		
		IDomainObject.IObjectOneToOneReference reference = getOneToOneReferenceFor(domainObject, thisJoinPointStaticPart);
		if (reference != null) {
			reference.notifyListeners(newReference);
		}
		
		// rather crude, see comments above.
		
		// temporarily moved up into transactionalChangingAttributeOnPojo...
//		domainObject.externalStateChanged();
	}


	/**
	 * If we are able to locate the {@link org.essentialplatform.session.IDomainObject}
	 * wrapper for this pojo then get it to notify any listeners it has for
	 * this collection.
	 * 
	 * <p>
	 * In addition, notify all {@link IObservedFeature}s of the session.
	 */
	after(IPojo pojo, IPojo addedObject): invokeAddToCollectionOnPojo(pojo, addedObject) {
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject == null || domainObject.getPersistState() == PersistState.UNKNOWN) {
			return;
		}
		
		IDomainObject.IObjectCollectionReference reference = getCollectionReferenceFor(domainObject, thisJoinPointStaticPart);
		if (reference != null) {
			reference.notifyListeners((Object)addedObject, true);
		}
		
		// rather crude, see comments above.
		
		// temporarily moved up into transactionalChangingAttributeOnPojo...
//		domainObject.externalStateChanged();
	}


	/**
	 * If we are able to locate the {@link org.essentialplatform.session.IDomainObject}
	 * wrapper for this pojo then get it to notify any listeners it has for
	 * this collection.
	 * 
	 * <p>
	 * In addition, notify all {@link IObservedFeature}s of the session.    
	 */
	after(IPojo pojo, Object removedObject): invokeRemoveFromCollectionOnPojo(pojo, removedObject) { 
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject == null || domainObject.getPersistState() == PersistState.UNKNOWN) {
			return;
		}
		
		IDomainObject.IObjectCollectionReference reference = getCollectionReferenceFor(domainObject, thisJoinPointStaticPart);
		if (reference != null) {
			reference.notifyListeners(removedObject, false);
		}
		
		// rather crude, see comments above.
		
		// temporarily moved up into transactionalChangingAttributeOnPojo...
//		domainObject.externalStateChanged();
	}


}
