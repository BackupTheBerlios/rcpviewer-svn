package org.essentialplatform.transaction.internal;

import org.essentialplatform.session.IDomainObject;
import org.essentialplatform.session.PojoAspect;
import org.essentialplatform.session.ISession;
import org.essentialplatform.session.IObservedFeature;
import org.essentialplatform.session.IPojo;
import org.essentialplatform.transaction.IChange;
import org.essentialplatform.transaction.ITransactable;
import org.essentialplatform.transaction.ITransaction;
import org.essentialplatform.transaction.ITransactionManager;
import org.essentialplatform.progmodel.standard.InDomain;

import java.lang.reflect.Field;

import org.apache.log4j.*;

/**
 * One change per invoked operation that hasn't been called from another
 * invoked operation.
 * 
 * <p>
 * TODO: this aspect may be wholly redundant?? 
 */
public aspect TransactionInvokeOperationAspect extends TransactionChangeAspect {

	private final static Logger LOG = Logger.getLogger(TransactionInvokeOperationAspect.class);
	private static boolean ENABLED = true;
	protected Logger getLogger() { return LOG; }
	protected boolean isEnabled() { return ENABLED; }
	
	declare precedence: TransactionInvokeOperationAspect, TransactionAttributeChangeAspect; 
	declare precedence: TransactionInvokeOperationAspect, TransactionOneToOneReferenceChangeAspect; 
	declare precedence: TransactionInvokeOperationAspect, TransactionAddToCollectionChangeAspect; 
	declare precedence: TransactionInvokeOperationAspect, TransactionRemoveFromCollectionChangeAspect; 
	
	protected pointcut changingPojo(IPojo pojo): invokeOperationOnPojo(pojo) ;

	protected pointcut transactionalChange(IPojo pojo): 
		changingPojo(pojo) &&
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
	Object around(IPojo pojo): transactionalChange(pojo) {
		getLogger().info("pojo=" + pojo);
		ITransactable transactable = (ITransactable)pojo;
		boolean transactionOnThread = hasTransactionForThread();
		ITransaction transaction = currentTransaction(transactable);
		if (!transactionOnThread) {
			getLogger().debug("no xactn for thread, setting; xactn=" + transaction);
			setTransactionForThread(transaction);
		} else {
			getLogger().debug("(xactn for thread already present)");
		}
		boolean startedInteraction = transaction.startingInteraction();
		try {
			return proceed(pojo);
		} finally {
			if (startedInteraction) {
				transaction.completingInteraction();
			}
			if (!transactionOnThread) {
				getLogger().debug("clearing xactn on thread; xactn=" + transaction);
				clearTransactionForThread();
			}
		}
	}


}
