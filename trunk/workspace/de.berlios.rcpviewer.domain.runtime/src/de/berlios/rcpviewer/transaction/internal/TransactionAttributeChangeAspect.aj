package de.berlios.rcpviewer.transaction.internal;

import de.berlios.rcpviewer.session.IPojo;
import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.ITransaction;

/**
 * One change per modified attribute performed directly (ie not programmatically
 * from an invoked operation).
 */
public aspect TransactionAttributeChangeAspect extends TransactionChangeAspect 
	percflow(transactionalChange(IPojo)) {
	
	private static boolean ENABLED = true;
	
	// TODO: can probably make abstract in TranactionChangeAspect
	pointcut transactionalChange(IPojo pojo): 
		modifyAttributeOnPojoNoArg(pojo) && !cflowbelow(invokeOperationOnPojo(IPojo)) ;

	/**
	 * Obtains transaction from either the thread or from the pojo (checking
	 * that they don't conflict)
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
