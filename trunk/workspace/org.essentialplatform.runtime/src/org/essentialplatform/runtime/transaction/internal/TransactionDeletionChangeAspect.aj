package org.essentialplatform.runtime.transaction.internal;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.changes.DeletionChange;
import org.essentialplatform.runtime.transaction.changes.IChange;

import java.util.concurrent.*;
import org.essentialplatform.runtime.domain.PojoAspect;
/**
 * One change per modified attribute performed directly (ie not programmatically
 * from an invoked operation).
 */
public aspect TransactionDeletionChangeAspect extends PojoAspect {

	private TransactionDeletionChangeAspectAdvice advice = 
		new TransactionDeletionChangeAspectAdvice();
	
	protected pointcut transactionalChange(IPojo pojo): 
		deletingPojoUsingDeleteMethod(pojo) &&
		!cflowbelow(invokeOperationOnPojo(IPojo)) ; 


	/**
	 * @see org.essentialplatform.runtime.transaction.internal.TransactionDeletionChangeAspectAdvice#around$transactionalChange(IPojo, Callable)
	 */
	Object around(final IPojo pojo): transactionalChange(pojo) {
		return advice.around$transactionalChange(
				pojo, 
				new Callable() {
					public Object call() {
						return proceed(pojo);			
					}
				});
	}

	/**
	 * @see org.essentialplatform.runtime.transaction.internal.TransactionDeletionChangeAspectAdvice#around$deletingPojoUsingDeleteMethod(IPojo)
	 */
	Object around(IPojo pojo): deletingPojoUsingDeleteMethod(pojo) {
		return advice.around$deletingPojoUsingDeleteMethod(pojo);
	}
	

}
