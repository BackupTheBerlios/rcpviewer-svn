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
import org.essentialplatform.runtime.util.JoinPointUtil;

class TransactionAttributeChangeAspectAdvice extends TransactionAspectAdvice {

	/**
	 * Defines interaction boundary.
	 */
	<V> V around$invokeSetterForAttributeOnPojo(IPojo pojo, Object postValue, Callable<V> proceed) {
		getLogger().debug("invokeSetterForAttributeOnPojo(pojo=" + pojo+"): start");
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
			getLogger().debug("invokeSetterForAttributeOnPojo(pojo=" + pojo+"): end");
		}
	}


	/**
	 * Creates an AttributeChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * In addition, passes the IDomainObject's IAttribute to the change so that
	 * it can notify listeners as it is executed/undone.  
	 *  
	 * <p>
	 * The change also notifies all {@link IObservedFeature}s of the session.  
	 * That's because a prerequisite of an operation or an attribute might 
	 * become satisfied (or no longer satisfied) as a result of this change.
	 * 
	 * <p>
	 * <n>Implementation notes</n>: informing all observed features seems rather
	 * crude.  An alternative design and possibly preferable approach would be 
	 * to wait until the current "workgroup" (as defined by the transaction 
	 * aspect) has completed.
	 *    
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied. 
	 */
	Object around$changingAttributeOnPojo(IPojo pojo, Object postValue, JoinPoint.StaticPart thisJoinPointStaticPart) { 
		getLogger().debug("changingAttributeOnPojo(pojo=" + pojo+", postValue='" + postValue + "'): start");
		try {
			Field field = JoinPointUtil.getFieldFor(thisJoinPointStaticPart);
			
			ITransactable transactable = (ITransactable)pojo;
			ITransaction transaction = currentTransaction(transactable);
			
			IDomainObject domainObject = pojo.getDomainObject();
			IDomainObject.IObjectAttribute attribute = null;
			if (domainObject.getPersistState() != PersistState.UNKNOWN) {
				attribute = JoinPointUtil.getAttributeFor(domainObject, thisJoinPointStaticPart);
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
