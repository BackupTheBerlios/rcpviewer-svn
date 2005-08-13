package de.berlios.rcpviewer.transaction.internal;

import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.session.PojoAspect;
import de.berlios.rcpviewer.session.IPojo;

/**
 * 
 * The original design called for this aspect to be scoped using
 * <code>percflow(invokeOperationOnPojo(IPojo))</code>.  However, as of
 * ajdt_1.2.1.20050708145019 this is failing (causing a reflection exception).
 */
public aspect TransactionAspect extends PojoAspect {
	
	/**
	 * Pojos should be transactable.
	 */
	declare parents: IPojo implements ITransactable;

	public String ITransactable.foo() { return "foo"; }

	pointcut transactionScope(IPojo pojo):
		invokeOperationOnPojo(pojo) /* && !within(TransactionAspect) */;

	Object around(IPojo pojo): invokeOperationOnPojo(pojo) {
		return proceed(pojo);
	}

//	Object around(IPojo pojo): transactionScope(pojo) {
//		ITransactable transactable = (ITransactable)pojo;
//		System.out.println("TransactionAspect: " + thisJoinPoint + ": enter + pojo.foo() = " + transactable.foo());
//		try {
//			return proceed(pojo);
//		} finally {
//			System.out.println("TransactionAspect: " + thisJoinPoint + ": exit");
//		}
//
//	}
	

}
