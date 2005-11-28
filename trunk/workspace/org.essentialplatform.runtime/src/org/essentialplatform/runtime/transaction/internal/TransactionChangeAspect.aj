package org.essentialplatform.runtime.transaction.internal;

import org.apache.log4j.Logger;

import org.essentialplatform.runtime.domain.DomainObject;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.domain.IObservedFeature;

import org.essentialplatform.runtime.session.ISession;

import org.essentialplatform.runtime.transaction.*;
import org.essentialplatform.runtime.transaction.changes.*;

/**
 * Defines versions of the inherited pointcuts (transactionalXxx) that also 
 * require that the pojo has been initialized.
 */
public abstract aspect TransactionChangeAspect extends TransactionAspect {

	/**
	 * Sub-aspects implement to control where logging goes.
	 */
	protected abstract Logger getLogger();
	
	/**
	 * Keeps track of the current transaction for this thread (if any)
	 */
	private static ThreadLocal<ITransaction> __transactionByThread;
	static {
		__transactionByThread = new ThreadLocal<ITransaction>() {
	        protected synchronized ITransaction initialValue() {
	            return null;
	        }
		};
	}
	
	/**
	 * Intent is for this to be called when a change (work group) has been completed.
	 * 
	 * <p>
	 * However, trying to invoke when change is complete was resulting in an
	 * infinite loop.
	 */
	protected void externalStateChangedForObservedFeatures(IPojo pojo) {
		
		IDomainObject<?> domainObject = pojo.getDomainObject();
		
		if (domainObject != null) {
			ISession session = domainObject.getSession();
			for(IObservedFeature observedFeature: session.getObservedFeatures()) {
				observedFeature.externalStateChanged();
			}
		}
	}
	
	/**
	 * Returns either the current transaction for this thread or the transaction
	 * in which the {@link ITransactable} has already been enlisted.
	 * 
	 * <p>
	 * If there is already a current transaction for both the thread and for
	 * the transactable object <b>and they don't match</b>, then discards the 
	 * change on the current transaction and throws a runtime exception.
	 * 
	 * <p>
	 * Before returning, also ensures that the transaction is in a state of
	 * either in progress or building state.
	 */
	protected ITransaction currentTransaction(ITransactable transactable) throws RuntimeException {
		ITransaction transaction = getTransactionForThreadIfAny();
		ITransaction pojoTransaction = transactable.getTransaction(false);
		if (transaction != null && 
		    pojoTransaction != null) {
			if (transaction != pojoTransaction) {
				// this mustn't happen
				throw new PojoAlreadyEnlistedException(transactable, pojoTransaction);
			}
			return transaction;
		} else if (transaction     != null &&
		           pojoTransaction == null) {
			// nothing to do; will use transaction on thread already
		} else if (transaction     == null &&
		           pojoTransaction != null) {
			transaction = pojoTransaction;
		} else {
			// ... if transaction == null && pojoTransaction == null
			transaction = getTransactionManagerImpl().createTransaction();
		}
		if (transaction != null) {
			// a bit of a hack, but createTransaction won't return a transaction if the
			// xact mgr has been suspended.
			transaction.checkInState(ITransaction.State.IN_PROGRESS, ITransaction.State.BUILDING_CHANGE);
		}
		return transaction;
	}
	
	protected ITransaction getTransactionForThreadIfAny() {
		return __transactionByThread.get();
	}

	/**
	 * Whether there is already a transaction for this thread.
	 */
	protected boolean hasTransactionForThread() {
		return getTransactionForThreadIfAny() != null;
	}

	protected void clearTransactionForThread() {
		__transactionByThread.set(null);
	}

	protected void setTransactionForThread(final ITransaction transaction) {
		__transactionByThread.set(transaction);
	}

	/**
	 * Whether the pojo can be enlisted in a transaction.
	 * 
	 * <p>
	 * We mustn't attempt to enlist a pojo that is being constructed.  The
	 * implementation of {@link DomainObject} is such we only set up its 
	 * persistence state and its resolve state at the end of its constructor.
	 * We will ignore objects whose state is not yet fully specified.
	 */
	protected static boolean canBeEnlisted(final IPojo pojo) {
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject == null) {
			return false;
		}
		return domainObject.getResolveState() != null &&
		       !domainObject.getResolveState().isUnknown() &&
		       domainObject.getPersistState() != null &&
		       !domainObject.getPersistState().isUnknown();
	}


	//////////////////////////////////////////////////////////////////
	// transactionalXxx versions of the pointcuts from PojoAspect that also check that the
	// pojo is in a state whereby it can be enlisted.
	//
	// note that there is no transactionalXxx version for instantiatingPojo; 
	// instead the TransactionInstantiationChange aspect defines its own
	// pointcut based on the session.
	//
	
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
	protected pointcut transactionalChangingAttributeOnPojo(IPojo pojo, Object postValue) :
		changingAttributeOnPojo(pojo, postValue) &&
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
	 * SHOULD APPEAR LEXICALLY BELOW THE invoke... POINTCUTS SINCE HAS LOWER
	 * PRECEDENCE. 
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 * 
	 */
	protected pointcut transactionalChangingOneToOneReferenceOnPojo(IPojo pojo, IPojo referencedObjectOrNull) :
		changingOneToOneReferenceOnPojo(pojo, referencedObjectOrNull) &&
		if(canBeEnlisted(pojo));  


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
	protected pointcut transactionalAddingToCollectionOnPojo(IPojo pojo, java.util.Collection collection, Object addedObj): 
		addingToCollectionOnPojo(pojo, collection, addedObj) &&
		if(canBeEnlisted(pojo));  

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
	protected pointcut transactionalRemovingFromCollectionOnPojo(IPojo pojo, java.util.Collection collection, Object removedObj): 
		removingFromCollectionOnPojo(pojo, collection, removedObj) &&
		if(canBeEnlisted(pojo));  


	/**
	 * Capture a pojo being deleted.
	 * 
	 * <p>
	 * Protected so that sub-aspects can use.
	 */
	protected pointcut transactionalDeletingPojoUsingDeleteMethod(IPojo pojo): 
		deletingPojoUsingDeleteMethod(pojo) && 
		if(canBeEnlisted(pojo));

	///////////////////////////////////////////////////////////////////////
	
}
