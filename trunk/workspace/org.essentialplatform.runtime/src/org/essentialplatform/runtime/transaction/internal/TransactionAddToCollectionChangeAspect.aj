package org.essentialplatform.runtime.transaction.internal;

import java.util.Collection;

import org.apache.log4j.Logger;

import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.transaction.*;
import org.essentialplatform.runtime.transaction.changes.*;
import org.essentialplatform.runtime.persistence.IPersistable;
import org.essentialplatform.runtime.persistence.IPersistable.PersistState;

public aspect TransactionAddToCollectionChangeAspect extends TransactionCollectionChangeAspect {
	
	private final static Logger LOG = Logger.getLogger(TransactionAddToCollectionChangeAspect.class);
	protected Logger getLogger() { return LOG; }

	// as required by super-aspect
	protected pointcut transactionalChange(IPojo pojo): 
		this(pojo) &&
		addingToCollectionOnPojo(IPojo, Collection, Object) && 
		!within(TransactionCollectionChangeAspect) && 
		if(canBeEnlisted(pojo)) &&
		!cflowbelow(invokeOperationOnPojo(IPojo)) ; 


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
	Object around(IPojo pojo, IPojo addedObject): invokeAddToCollectionOnPojo(pojo, addedObject) {
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject.getPersistState() == PersistState.UNKNOWN) {
			return proceed(pojo, addedObject);
		} else {
			IDomainObject.IObjectCollectionReference reference = 
				getCollectionReferenceFor(domainObject, thisJoinPointStaticPart);
			
			setCollectionReferenceForThread(reference);
			try {
				return proceed(pojo, addedObject);
			} finally {
				clearCollectionReferenceForThread();
			}
		}
	}

	
	
	/**
	 * Creates an AddToCollectionChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied.
	 *  
	 * <p>
	 * TODO: how pick up the collection name?
	 */
	Object around(IPojo pojo, Collection collection, Object addedObj): 
			transactionalAddingToCollectionOnPojo(pojo, collection, addedObj) {
		ITransactable transactable = (ITransactable)pojo;
		ITransaction transaction = currentTransaction(transactable);
		IChange change = new AddToCollectionChange(transaction, transactable, collection, addedObj, getCollectionReferenceForThreadIfAny());
		return change.execute();
	}

	
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
	

}
