package de.berlios.rcpviewer.transaction.internal;

import java.util.Collection;

import org.apache.log4j.Logger;

import de.berlios.rcpviewer.session.IPojo;
import de.berlios.rcpviewer.transaction.IChange;
import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.ITransaction;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.transaction.PojoAlreadyEnlistedException;

public aspect TransactionAddToCollectionChangeAspect extends TransactionCollectionChangeAspect {
	
	private final static Logger LOG = Logger.getLogger(TransactionAddToCollectionChangeAspect.class);
	protected Logger getLogger() { return LOG; }

	// as required by super-aspect
	protected pointcut transactionalChange(IPojo pojo): 
		changingPojo(pojo, Collection) &&
		if(canBeEnlisted(pojo)) &&
		!cflowbelow(invokeOperationOnPojo(IPojo)) ; 

	private pointcut changingPojo(IPojo pojo, Collection collection): 
		this(pojo) &&
		target(collection) &&
		addingToCollectionOnPojo(IPojo, Collection, Object) && 
		!within(TransactionCollectionChangeAspect); 


	/**
	 * Obtains transaction from either the thread or from the pojo (checking
	 * that they don't conflict).
	 * 
	 * <p>
	 * This code is identical in all subaspects of TransactionChange, however
	 * moving it up and declaring a precedence doesn't seem to do the trick.
	 */
	Object around(IPojo pojo): transactionalChange(pojo) {
		getLogger().info("pojo=" + pojo);
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
	Object around(IPojo pojo, Collection collection, Object addedObj): addingToCollectionOnPojo(pojo, collection, addedObj) {
		ITransactable transactable = (ITransactable)pojo;
		ITransaction transaction = currentTransaction(transactable);
		IChange change = new AddToCollectionChange(transaction, transactable, collection, "???", addedObj);
		
//		IDomainObject<?> domainObject = pojo.getDomainObject();
//		// only if we have a domain object (ie fully instantiated) and
//		// are attached to a session do we check.
//		if (domainObject != null && domainObject.isAttached()) {
//			if (!transaction.addingToInteractionChangeSet(change)) {
//				throw new PojoAlreadyEnlistedException();			
//			}
//		}
//
		return change.execute();
	}


}
