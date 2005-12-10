package org.essentialplatform.runtime.transaction.internal;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;

import java.util.concurrent.*;
import org.essentialplatform.runtime.domain.PojoAspect;
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
	 * Obtains transaction from either the thread or from the pojo (checking
	 * that they don't conflict).
	 * 
	 * <p>
	 * This code is identical in all subaspects of TransactionChange, however
	 * moving it up and declaring a precedence doesn't seem to do the trick.
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
