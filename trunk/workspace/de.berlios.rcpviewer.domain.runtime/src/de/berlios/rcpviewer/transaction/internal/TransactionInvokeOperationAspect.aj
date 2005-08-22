package de.berlios.rcpviewer.transaction.internal;

import de.berlios.rcpviewer.session.IPojo;
import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.ITransaction;


/**
 * One change per invoked operation that hasn't been called from another
 * invoked operation.
 */
public aspect TransactionInvokeOperationAspect extends TransactionChangeAspect 
	percflow(transactionalChange(IPojo)) {

	private static boolean ENABLED = true;

	// TODO: can probably make abstract in TranactionChangeAspect
	pointcut transactionalChange(IPojo pojo): 
		invokeOperationOnPojo(pojo) && !cflowbelow(invokeOperationOnPojo(IPojo)) ;

	/**
	 * Gets the current transaction for pojo (creating one if necessary) and
	 * sets for the thread. 
	 */
	Object around(IPojo pojo): transactionalChange(pojo) && if(ENABLED) {
		ITransactable transactable = (ITransactable)pojo;
		boolean transactionOnThread = hasTransactionForThread();
		ITransaction transaction = currentTransaction(transactable);
		if (!transactionOnThread) {
			setTransactionForThread(transaction);
		}
		transaction.startChange();
		System.out.println(transaction); // TODO: remove
		try {
			return proceed(pojo);
		} finally {
			transaction.completeChange();
			System.out.println(transaction); // TODO: remove
			if (!transactionOnThread) {
				clearTransactionForThread();
			}
		}
	}

}
