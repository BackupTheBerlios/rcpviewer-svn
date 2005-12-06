package org.essentialplatform.runtime.transaction.internal;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

import org.aspectj.lang.*;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.changes.AttributeChange;
import org.essentialplatform.runtime.transaction.changes.IChange;
import org.essentialplatform.runtime.util.AspectsUtil;

class TransactionAttributeChangeAspectAdvice extends TransactionAspectAdvice {

	/**
	 * Obtains transaction from either the thread or from the pojo (checking
	 * that they don't conflict).
	 * 
	 * <p>
	 * This code is identical in all subaspects of TransactionChange, however
	 * moving it up and declaring a precedence doesn't seem to do the trick.
	 */
	<V> V around$transactionalChange(IPojo pojo, Callable<V> proceed) {
		getLogger().debug("transactionalChange(pojo=" + pojo+"): start");
		ITransactable transactable = (ITransactable)pojo;
		boolean transactionOnThread = ThreadLocals.hasTransactionForThread();
		ITransaction transaction = currentTransaction(transactable);
		if (!transactionOnThread) {
			getLogger().debug("no xactn for thread, setting; xactn=" + transaction);
			ThreadLocals.setTransactionForThread(transaction);
		} else {
			getLogger().debug("(xactn for thread already present)");
		}
		boolean startedInteraction = transaction.startingInteraction();
		try {
			return call(proceed);
		} finally {
			if (startedInteraction) {
				transaction.completingInteraction();
			}
			if (!transactionOnThread) {
				getLogger().debug("clearing xactn on thread; xactn=" + transaction);
				ThreadLocals.clearTransactionForThread();
			}
			getLogger().debug("transactionalChange(pojo=" + pojo+"): end");
		}
	}


	Object around$transactionalChangingAttributeOnPojo(IPojo pojo, Object postValue, JoinPoint.StaticPart thisJoinPointStaticPart) { 
		getLogger().debug("changingAttributeOnPojo(pojo=" + pojo+", postValue='" + postValue + "'): start");
		try {
			Field field = AspectsUtil.getFieldFor(thisJoinPointStaticPart);
			
			ITransactable transactable = (ITransactable)pojo;
			ITransaction transaction = currentTransaction(transactable);
			
			IDomainObject domainObject = pojo.getDomainObject();
			IDomainObject.IObjectAttribute attribute = null;
			if (domainObject.getPersistState() != PersistState.UNKNOWN) {
				attribute = AspectsUtil.getAttributeFor(domainObject, thisJoinPointStaticPart);
			}
			IChange change = new AttributeChange(transaction, transactable, field, postValue, attribute);
			
			return change.execute();
		} finally {
			getLogger().debug("changingAttributeOnPojo(pojo=" + pojo+", postValue='" + postValue + "'): end");
		}
	}

	@Override
	protected Logger getLogger() {
		return Logger.getLogger(TransactionAttributeChangeAspectAdvice.class);
	}
	


}
