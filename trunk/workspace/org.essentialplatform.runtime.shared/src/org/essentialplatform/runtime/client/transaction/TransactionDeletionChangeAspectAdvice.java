package org.essentialplatform.runtime.client.transaction;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.client.transaction.changes.DeletionChange;
import org.essentialplatform.runtime.client.transaction.changes.IChange;
import org.essentialplatform.runtime.shared.domain.IPojo;

class TransactionDeletionChangeAspectAdvice extends TransactionAspectAdvice {

	/**
	 * Defines interaction boundary.
	 */
	Object around$transactionalChange(IPojo pojo, Callable proceed) {
		getLogger().debug("transactionalChange(pojo=" + pojo+"): start");
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
			return call(proceed);
		} finally {
			if (startedInteraction) {
				transaction.completingInteraction();
			}
			if (!transactionOnThread) {
				getLogger().debug("clearing xactn on thread; xactn=" + transaction);
				ThreadLocals.clearTransactionForThread();
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
	Object around$deletingPojoUsingDeleteMethod(IPojo pojo) {
		getLogger().debug("deletingPojoUsingDeleteMethod(pojo=" + pojo+"): start");
		ITransaction transaction = currentTransaction(pojo);
		IChange change = new DeletionChange(transaction, pojo);
		try {
			return change.execute();
		} finally {
			getLogger().debug("deletingPojoUsingDeleteMethod(pojo=" + pojo+"): end");
		}
	}

	
	@Override
	protected Logger getLogger() {
		return Logger.getLogger(TransactionDeletionChangeAspectAdvice.class);
	}

}


