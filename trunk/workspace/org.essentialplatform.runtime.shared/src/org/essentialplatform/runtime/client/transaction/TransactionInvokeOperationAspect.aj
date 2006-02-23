package org.essentialplatform.runtime.client.transaction;

import org.apache.log4j.Logger;

import java.util.concurrent.Callable;

import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.PojoAspect;
import org.essentialplatform.runtime.shared.domain.InteractionsAspect;

/**
 * One change per invoked operation that hasn't been called from another
 * invoked operation.
 */
public aspect TransactionInvokeOperationAspect extends InteractionsAspect {

	private final static Logger LOG = Logger.getLogger(TransactionInvokeOperationAspect.class);
	protected Logger getLogger() { return LOG; }

	private static boolean ENABLED = true;
	protected boolean isEnabled() { return ENABLED; }

	private TransactionInvokeOperationAspectAdvice advice = 
		new TransactionInvokeOperationAspectAdvice();
	
	declare precedence: PojoAspect, TransactionInvokeOperationAspect; 

	declare precedence: TransactionInvokeOperationAspect, TransactionAttributeChangeAspect; 
	declare precedence: TransactionInvokeOperationAspect, TransactionOneToOneReferenceChangeAspect; 
	declare precedence: TransactionInvokeOperationAspect, TransactionAddToCollectionChangeAspect; 
	declare precedence: TransactionInvokeOperationAspect, TransactionRemoveFromCollectionChangeAspect; 
	
	protected pointcut transactionalChange(IPojo pojo): 
		invokeOperationOnPojo(pojo) &&
		if(canBeInteractedWith(pojo)) &&
		!cflowbelow(invokeOperationOnPojo(IPojo)) ; 

	/**
	 * @see org.essentialplatform.runtime.transaction.internal.TransactionInvokeOperationAspectAdvice#around$transactionalChange(IPojo, Callable)
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


}
