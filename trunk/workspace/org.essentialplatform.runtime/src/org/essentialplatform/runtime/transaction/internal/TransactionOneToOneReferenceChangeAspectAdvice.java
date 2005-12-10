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
import org.essentialplatform.runtime.transaction.changes.OneToOneReferenceChange;
import org.essentialplatform.runtime.transaction.changes.RemoveFromCollectionChange;
import org.essentialplatform.runtime.util.JoinPointUtil;
import org.essentialplatform.runtime.util.ReflectUtil;

class TransactionOneToOneReferenceChangeAspectAdvice extends TransactionAspectAdvice {

	/**
	 * Obtains transaction from either the thread or from the pojo (checking
	 * that they don't conflict).
	 * 
	 * <p>
	 * This code is identical in all subaspects of TransactionChange, however
	 * moving it up and declaring a precedence doesn't seem to do the trick.
	 */
	Object around$transactionalChange(final IPojo pojo, Callable proceed) {
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
		}
	}

	/**
	 * Creates an OneToOneReferenceChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied. 
	 */
	Object around$changingOneToOneReferenceOnPojo(
			final IPojo pojo, final IPojo referencedObjOrNull, JoinPoint.StaticPart thisJoinPointStaticPart) {
		getLogger().debug("changingOneToOneReferenceOnPojo(pojo=" + pojo+")");
		Field field = JoinPointUtil.getFieldFor(thisJoinPointStaticPart);
		ITransactable transactable = (ITransactable)pojo;
		ITransaction transaction = currentTransaction(transactable);

		IDomainObject domainObject = pojo.getDomainObject();
		IDomainObject.IObjectOneToOneReference reference = null;
		if (domainObject.getPersistState() != PersistState.UNKNOWN) {
			reference = JoinPointUtil.getOneToOneReferenceFor(domainObject, thisJoinPointStaticPart);
		}
		IChange change = new OneToOneReferenceChange(transaction, transactable, field, referencedObjOrNull, reference);
		
		return change.execute();
	}


	
	@Override
	protected Logger getLogger() {
		return Logger.getLogger(TransactionOneToOneReferenceChangeAspectAdvice.class);
	}

}
