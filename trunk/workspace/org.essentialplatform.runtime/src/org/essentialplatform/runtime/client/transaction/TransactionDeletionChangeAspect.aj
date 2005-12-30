package org.essentialplatform.runtime.client.transaction;

import java.util.concurrent.Callable;

import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.PojoAspect;

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
