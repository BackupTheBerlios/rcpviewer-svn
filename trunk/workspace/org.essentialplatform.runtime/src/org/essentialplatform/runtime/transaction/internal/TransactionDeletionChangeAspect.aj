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
	
	private final static Logger LOG = Logger.getLogger(TransactionDeletionChangeAspect.class);
	protected Logger getLogger() { return LOG; }

	protected pointcut transactionalChange(IPojo pojo): 
		deletingPojoUsingDeleteMethod(pojo) &&
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

	/**
	 * Creates an AttributeChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied. 
	 */
	Object around(IPojo pojo): deletingPojoUsingDeleteMethod(pojo) {
		return advice.around$deletingPojoUsingDeleteMethod(pojo);
	}
	

}
