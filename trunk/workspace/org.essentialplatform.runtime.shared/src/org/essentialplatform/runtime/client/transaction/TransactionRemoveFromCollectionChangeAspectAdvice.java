package org.essentialplatform.runtime.client.transaction;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.essentialplatform.runtime.client.transaction.changes.IChange;
import org.essentialplatform.runtime.client.transaction.changes.RemoveFromCollectionChange;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.util.JoinPointUtil;

class TransactionRemoveFromCollectionChangeAspectAdvice extends TransactionAspectAdvice {

	
	/**
	 * Defines the boundaries of the interaction.
	 * 
	 * <p>
	 * In addition, binds the name of the collection (derived from the
	 * method name) to a ThreadLocal so that the actual change to the
	 * collection (within {@link #around$addingToCollectionOnPojo(IPojo, Collection, Object)})
	 * can provide the collection name.
	 */
	Object around$invokeRemoveFromCollectionOnPojo(IPojo pojo, Object removedObject, JoinPoint.StaticPart thisJoinPointStaticPart, Callable proceed) { 

		getLogger().debug("invokeRemoveFromCollectionOnPojo(pojo=" + pojo+"): start");
		boolean transactionOnThread = ThreadLocals.hasTransactionForThread();
		ITransaction transaction = currentTransaction(pojo);
		if (!transactionOnThread) {
			getLogger().debug("no xactn for thread, setting; xactn=" + transaction);
			ThreadLocals.setTransactionForThread(transaction);
		} else {
			getLogger().debug("(xactn for thread already present)");
		}
		boolean startedInteraction = transaction.startingInteraction();
		try {
			IDomainObject domainObject = pojo.domainObject();
			
			IDomainObject.IObjectCollectionReference reference = JoinPointUtil.getCollectionReferenceFor(domainObject, thisJoinPointStaticPart);
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
			getLogger().debug("invokeRemoveFromCollectionOnPojo(pojo=" + pojo+"): end");
		}
		
	}

	
	/**
	 * Creates a RemoveFromCollectionChange to wrap a change to the attribute, adding it
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
	Object around$removingFromCollectionOnPojo(IPojo pojo, Collection collection, Object removedObj) {
		getLogger().debug("removingFromCollectionOnPojo(pojo=" + pojo+"): start");
		ITransaction transaction = currentTransaction(pojo);
		IChange change = new RemoveFromCollectionChange(transaction, pojo, collection, removedObj, ThreadLocals.getCollectionReferenceForThreadIfAny());
		try {
			return change.execute();
		} finally {
			getLogger().debug("removingFromCollectionOnPojo(pojo=" + pojo+"): end");
		}
	}

	
	@Override
	protected Logger getLogger() {
		return Logger.getLogger(TransactionRemoveFromCollectionChangeAspectAdvice.class);
	}

}
