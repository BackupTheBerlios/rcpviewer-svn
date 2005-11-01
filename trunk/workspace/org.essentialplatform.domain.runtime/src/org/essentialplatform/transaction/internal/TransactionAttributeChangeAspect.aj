package org.essentialplatform.transaction.internal;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import org.essentialplatform.session.IPojo;
import org.essentialplatform.transaction.IChange;
import org.essentialplatform.transaction.ITransactable;
import org.essentialplatform.transaction.ITransaction;


/**
 * One change per modified attribute performed directly (ie not programmatically
 * from an invoked operation).
 */
public aspect TransactionAttributeChangeAspect extends TransactionChangeAspect 
	/* percflow(transactionalChange(IPojo)) */ {

	private final static Logger LOG = Logger.getLogger(TransactionAttributeChangeAspect.class);
	protected Logger getLogger() { return LOG; }

	protected pointcut changingPojo(IPojo pojo): changingAttributeOnPojo(pojo, Object); 

	protected pointcut transactionalChange(IPojo pojo): 
		changingPojo(pojo) &&
		if(canBeEnlisted(pojo)) &&
		!cflowbelow(invokeOperationOnPojo(IPojo)) ;  // this is probably unnecessary since the invokeOperation aspect has precedence over this one...

	/**
	 * Obtains transaction from either the thread or from the pojo (checking
	 * that they don't conflict).
	 * 
	 * <p>
	 * This code is identical in all subaspects of TransactionChange, however
	 * moving it up and declaring a precedence doesn't seem to do the trick.
	 */
	Object around(IPojo pojo): transactionalChange(pojo) {
		getLogger().debug("transactionalChange(pojo=" + pojo+"): start");
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
			getLogger().debug("transactionalChange(pojo=" + pojo+"): end");
		}
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
	Object around(IPojo pojo, Object postValue): changingAttributeOnPojo(pojo, postValue) {
		getLogger().debug("changingAttributeOnPojo(pojo=" + pojo+", postValue='" + postValue + "'): start");
		try {
			Field field = getFieldFor(thisJoinPointStaticPart);
			ITransactable transactable = (ITransactable)pojo;
			ITransaction transaction = currentTransaction(transactable);
			IChange change = new AttributeChange(transaction, transactable, field, postValue);
			return change.execute();
		} finally {
			getLogger().debug("changingAttributeOnPojo(pojo=" + pojo+", postValue='" + postValue + "'): end");
		}

	}
	

}
