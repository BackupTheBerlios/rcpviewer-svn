package de.berlios.rcpviewer.transaction.internal;

import java.lang.reflect.Field;

import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.PojoAspect;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.IObservedFeature;
import de.berlios.rcpviewer.session.IPojo;
import de.berlios.rcpviewer.transaction.IChange;
import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.ITransaction;
import de.berlios.rcpviewer.transaction.ITransactionManager;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

import org.apache.log4j.Logger;

/**
 * One change per modified 1:1 reference performed directly (ie not programmatically
 * from an invoked operation).
 */
public aspect TransactionCollectionChangeAspect extends TransactionChangeAspect 
	percflow(transactionalChange(IPojo)) {
	
	private final static Logger LOG = Logger.getLogger(TransactionCollectionChangeAspect.class);
	protected Logger getLogger() { return LOG; }

	protected pointcut changingPojo(IPojo pojo): 
		addingToCollectionOnPojo(pojo, IPojo) || 
		removingFromCollectionOnPojo(pojo, IPojo);

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
	 */
	Object around(IPojo pojo, IPojo addedObj): addingToCollectionOnPojo(pojo, addedObj) {
		Field field = getFieldFor(thisJoinPointStaticPart);
		ITransactable transactable = (ITransactable)pojo;
		ITransaction transaction = currentTransaction(transactable);
		IChange change = new AddToCollectionChange(transactable, field, addedObj);
		if (!transaction.addingToInteractionChangeSet(change)) {
			return null; // pojo already enlisted			
		}
		return proceed(pojo, addedObj); // equivalent to executing the change
	}

	/**
	 * Creates a RemoveFromCollectionChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied. 
	 */
	Object around(IPojo pojo, IPojo removedObj): removingFromCollectionOnPojo(pojo, removedObj) {
		Field field = getFieldFor(thisJoinPointStaticPart);
		ITransactable transactable = (ITransactable)pojo;
		ITransaction transaction = currentTransaction(transactable);
		IChange change = new RemoveFromCollectionChange(transactable, field, removedObj);
		if (!transaction.addingToInteractionChangeSet(change)) {
			return null; // pojo already enlisted			
		}
		return proceed(pojo, removedObj); // equivalent to executing the change
	}


}
