package org.essentialplatform.runtime.transaction.internal;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.aspectj.lang.*;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.changes.AddToCollectionChange;
import org.essentialplatform.runtime.transaction.changes.AttributeChange;
import org.essentialplatform.runtime.transaction.changes.IChange;
import org.essentialplatform.runtime.transaction.changes.InstantiationChange;
import org.essentialplatform.runtime.transaction.changes.OneToOneReferenceChange;
import org.essentialplatform.runtime.transaction.changes.RemoveFromCollectionChange;
import org.essentialplatform.runtime.util.JoinPointUtil;
import org.essentialplatform.runtime.util.ReflectUtil;

class TransactionInstantiationChangeAspectAdvice extends TransactionAspectAdvice {

	/**
	 * Defines interaction boundary.
	 */
	Object around$transactionalChange(IPojo pojo, Callable proceed) {
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

	/**
	 * Creates an InstantiationChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied. 
	 */
	Object around$creatingOrRecreatingPojo(IPojo pojo) {
		getLogger().debug("creatingOrRecreatingPojo(pojo=" + pojo+"): start");
		ITransactable transactable = (ITransactable)pojo;
		ITransaction transaction = currentTransaction(transactable);
		IChange change = new InstantiationChange(transaction, transactable);
		try {
			return change.execute();
		} finally {
			getLogger().debug("creatingOrRecreatingPojo(pojo=" + pojo+"): end");
		}
	}

	
	@Override
	protected Logger getLogger() {
		return Logger.getLogger(TransactionInstantiationChangeAspectAdvice.class);
	}

}


