package org.essentialplatform.runtime.client.transaction;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.transaction.ITransactable;
import org.essentialplatform.runtime.shared.transaction.ITransaction;
import org.essentialplatform.runtime.shared.transaction.changes.DeletionChange;
import org.essentialplatform.runtime.shared.transaction.changes.IChange;

class TransactionDeletionChangeAspectAdvice extends TransactionAspectAdvice {

	/**
	 * Defines interaction boundary.
	 */
	Object around$transactionalChange(IPojo pojo, Callable proceed) {
		getLogger().debug("transactionalChange(pojo=" + pojo+"): start");
		ITransactable transactable = (ITransactable)pojo;
		boolean transactionOnThread = ThreadLocals.hasTransactionForThread();
		ITransaction transaction = currentTransaction(transactable);
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
		ITransactable transactable = (ITransactable)pojo;
		ITransaction transaction = currentTransaction(transactable);
		IChange change = new DeletionChange(transaction, transactable);
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


