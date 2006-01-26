package org.essentialplatform.runtime.client.transaction;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.domain.IPojo;

class TransactionInvokeOperationAspectAdvice extends TransactionAspectAdvice {

	/**
	 * Obtains transaction from either the thread or from the pojo (checking
	 * that they don't conflict).
	 * 
	 * <p>
	 * This code is identical in all subaspects of TransactionChange, however
	 * moving it up and declaring a precedence doesn't seem to do the trick.
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


	
	@Override
	protected Logger getLogger() {
		return Logger.getLogger(TransactionInvokeOperationAspectAdvice.class);
	}

}
