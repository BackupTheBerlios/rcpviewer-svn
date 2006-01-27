package org.essentialplatform.runtime.client.transaction;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.essentialplatform.runtime.client.transaction.changes.AddToCollectionChange;
import org.essentialplatform.runtime.client.transaction.changes.IChange;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.util.JoinPointUtil;

class TransactionAddToCollectionChangeAspectAdvice extends TransactionAspectAdvice {



	/**
	 * Defines the boundaries of the interaction.
	 * 
	 * <p>
	 * In addition, binds the name of the collection (derived from the
	 * method name) to a ThreadLocal so that the actual change to the
	 * collection (within {@link #around$addingToCollectionOnPojo(IPojo, Collection, Object)})
	 * can provide the collection name.
	 */
	Object around$invokeAddToCollectionOnPojo(
			IPojo pojo, IPojo addedObject, JoinPoint.StaticPart thisJoinPointStaticPart, Callable proceed) {

		getLogger().debug("invokeAddToCollectionOnPojo(pojo=" + pojo+"): start");
		boolean transactionOnThread = ThreadLocals.hasTransactionForThread();
		ITransaction transaction = TransactionManager.instance().getCurrentTransactionFor(pojo);
		if (!transactionOnThread) {
			getLogger().debug("no xactn for thread, setting; xactn=" + transaction);
			ThreadLocals.setTransactionForThread(transaction);
		} else {
			getLogger().debug("(xactn for thread already present)");
		}
		boolean startedInteraction = transaction.startingInteraction();

		try {
			IDomainObject domainObject = pojo.domainObject();
			
			IDomainObject.IObjectCollectionReference reference = 
				JoinPointUtil.getCollectionReferenceFor(domainObject, thisJoinPointStaticPart);
			
			ThreadLocals.setCollectionReferenceForThread(reference);
			try {
				return call(proceed);
			} finally {
				ThreadLocals.clearCollectionReferenceForThread();
			}

		} finally {
			if (startedInteraction) {
				transaction.completingInteraction();
			}
			if (!transactionOnThread) {
				getLogger().debug("clearing xactn on thread; xactn=" + transaction);
				ThreadLocals.clearTransactionForThread();
			}
			getLogger().debug("invokeAddToCollectionOnPojo(pojo=" + pojo+"): end");
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
	 * Note the trick for picking up the collection name: from ThreadLocals.
	 */
	Object around$addingToCollectionOnPojo(
			IPojo pojo, Collection collection, Object addedObj) {
		getLogger().debug("addingToCollectionOnPojo(pojo=" + pojo+"): start");
		ITransaction transaction = currentTransaction(pojo);
		IChange change = new AddToCollectionChange(transaction, pojo, collection, addedObj, ThreadLocals.getCollectionReferenceForThreadIfAny());
		try {
			return change.execute();
		} finally {
			getLogger().debug("addingToCollectionOnPojo(pojo=" + pojo+"): end");
		}
	}

	@Override
	protected Logger getLogger() {
		return Logger.getLogger(TransactionAddToCollectionChangeAspectAdvice.class);
	}


}
