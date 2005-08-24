package de.berlios.rcpviewer.transaction.internal;

import de.berlios.rcpviewer.session.IPojo;
import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.ITransaction;
import org.apache.log4j.*;

/**
 * One change per invoked operation that hasn't been called from another
 * invoked operation.
 */
public aspect TransactionInvokeOperationAspect extends TransactionChangeAspect 
	percflow(transactionalChange(IPojo)) {

	private final static Logger LOG = Logger.getLogger(TransactionInvokeOperationAspect.class);
	
	private static boolean ENABLED = true;

	// TODO: can probably make abstract in TranactionChangeAspect
	pointcut transactionalChange(IPojo pojo): 
		invokeOperationOnPojo(pojo) && !cflowbelow(invokeOperationOnPojo(IPojo)) ;

	/**
	 * Gets the current transaction for pojo (creating one if necessary) and
	 * sets for the thread. 
	 */
	Object around(IPojo pojo): transactionalChange(pojo) && if(ENABLED) {
		LOG.info("pojo=" + pojo);
		ITransactable transactable = (ITransactable)pojo;
		boolean transactionOnThread = hasTransactionForThread();
		ITransaction transaction = currentTransaction(transactable);
		if (!transactionOnThread) {
			LOG.debug("no xactn for thread, setting; xactn=" + transaction);
			setTransactionForThread(transaction);
		} else {
			LOG.debug("(xactn for thread already present)");
		}
		transaction.startChange();
		try {
			return proceed(pojo);
		} finally {
			transaction.completeChange();
			if (!transactionOnThread) {
				LOG.debug("clearing xactn on thread; xactn=" + transaction);
				clearTransactionForThread();
			}
		}
	}

}
