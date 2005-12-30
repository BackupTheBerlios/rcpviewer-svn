package org.essentialplatform.runtime.shared.transaction.internal;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.PojoAspect;

/**
 * One change per invoked operation that hasn't been called from another
 * invoked operation.
 * 
 * <p>
 * TODO: this aspect may be wholly redundant?? 
 */
public aspect TransactionInvokeOperationAspect extends PojoAspect {

	private TransactionInvokeOperationAspectAdvice advice = 
		new TransactionInvokeOperationAspectAdvice();
	
	private final static Logger LOG = Logger.getLogger(TransactionInvokeOperationAspect.class);
	private static boolean ENABLED = true;
	protected Logger getLogger() { return LOG; }
	protected boolean isEnabled() { return ENABLED; }
	
	declare precedence: TransactionInvokeOperationAspect, TransactionAttributeChangeAspect; 
	declare precedence: TransactionInvokeOperationAspect, TransactionOneToOneReferenceChangeAspect; 
	declare precedence: TransactionInvokeOperationAspect, TransactionAddToCollectionChangeAspect; 
	declare precedence: TransactionInvokeOperationAspect, TransactionRemoveFromCollectionChangeAspect; 
	
	protected pointcut transactionalChange(IPojo pojo): 
		invokeOperationOnPojo(pojo) &&
		if(canBeEnlisted(pojo)) &&
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
