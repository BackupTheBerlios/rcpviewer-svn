package org.essentialplatform.runtime.transaction.internal;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import org.essentialplatform.runtime.domain.*;
import org.essentialplatform.runtime.session.ISession;
import org.essentialplatform.runtime.transaction.*;
import org.essentialplatform.runtime.transaction.changes.*;
import org.essentialplatform.runtime.persistence.IPersistable.PersistState;

import org.aspectj.lang.Signature;
import org.eclipse.emf.ecore.EAttribute;
import org.aspectj.lang.JoinPoint;


import java.util.Collection;
import org.apache.log4j.Logger;



/**
 * One change per modified attribute performed directly (ie not programmatically
 * from an invoked operation).
 */
public aspect TransactionAttributeChangeAspect extends TransactionChangeAspect 
	/* percflow(transactionalChange(IPojo)) */ {

	private final static Logger LOG = Logger.getLogger(TransactionAttributeChangeAspect.class);
	protected Logger getLogger() { return LOG; }

	protected pointcut changingPojo(IPojo pojo): changingAttributeOnPojo(pojo, Object); 

	protected pointcut transactionalChange(IPojo pojo): 
		changingPojo(pojo) &&
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
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied. 
	 */
	Object around(IPojo pojo, Object postValue): changingAttributeOnPojo(pojo, postValue) {
		getLogger().debug("changingAttributeOnPojo(pojo=" + pojo+", postValue='" + postValue + "'): start");
		try {
			Field field = getFieldFor(thisJoinPointStaticPart);
			ITransactable transactable = (ITransactable)pojo;
			ITransaction transaction = currentTransaction(transactable);
			IChange change = new AttributeChange(transaction, transactable, field, postValue);
			return change.execute();
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
	 * this attribute.
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
	after(IPojo pojo, Object newValue): changingAttributeOnPojo(pojo, newValue) { 
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject == null || domainObject.getPersistState() == PersistState.UNKNOWN) {
			return;
		}
		
		IDomainObject.IObjectAttribute attribute = getAttributeFor(domainObject, thisJoinPointStaticPart);
		if (attribute != null) {
			attribute.notifyListeners(newValue);
		}
		
		// rather crude, see comments above.
		ISession session = domainObject.getSession();
		for(IObservedFeature observedFeature: session.getObservedFeatures()) {
			observedFeature.externalStateChanged();
		}

	}


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
		ISession session = domainObject.getSession();
		for(IObservedFeature observedFeature: session.getObservedFeatures()) {
			observedFeature.externalStateChanged();
		}
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
		ISession session = domainObject.getSession();
		for(IObservedFeature observedFeature: session.getObservedFeatures()) {
			observedFeature.externalStateChanged();
		}
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
		ISession session = domainObject.getSession();
		for(IObservedFeature observedFeature: session.getObservedFeatures()) {
			observedFeature.externalStateChanged();
		}
	}


}
