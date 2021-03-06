package org.essentialplatform.runtime.client.transaction;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.essentialplatform.runtime.client.transaction.changes.IChange;
import org.essentialplatform.runtime.client.transaction.changes.OneToOneReferenceChange;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.util.JoinPointUtil;

class TransactionOneToOneReferenceChangeAspectAdvice extends TransactionAspectAdvice {

	
	/**
	 * Defines interaction boundary.
	 */
	Object around$invokeAssociatorForOneToOneReferenceOnPojo(IPojo pojo, IPojo newReferencedObject, Callable proceed) {
		getLogger().debug("invokeAssociatorForOneToOneReferenceOnPojo(pojo=" + pojo+"): start");
		boolean transactionOnThread = ThreadLocals.hasTransactionForThread();
		ITransaction transaction = currentTransaction(pojo);
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
			getLogger().debug("invokeAssociatorForOneToOneReferenceOnPojo(pojo=" + pojo+"): end");
		}
	}


	/**
	 * Defines interaction boundary.
	 */
	Object around$invokeDissociatorForOneToOneReferenceOnPojo(IPojo pojo, IPojo existingReferencedObject, Callable proceed) {
		getLogger().debug("invokeDissociatorForOneToOneReferenceOnPojo(pojo=" + pojo+"): start");
		boolean transactionOnThread = ThreadLocals.hasTransactionForThread();
		ITransaction transaction = currentTransaction(pojo);
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
			getLogger().debug("invokeDissociatorForOneToOneReferenceOnPojo(pojo=" + pojo+"): end");
		}
	}


	/**
	 * Creates an OneToOneReferenceChange to wrap a change to the reference, 
	 * adding it to the current transaction.
	 *  
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied. 
	 */
	Object around$changingOneToOneReferenceOnPojo(
			final IPojo pojo, final IPojo referencedObjOrNull, JoinPoint.StaticPart thisJoinPointStaticPart) {
		getLogger().debug("changingOneToOneReferenceOnPojo(pojo=" + pojo+"): start");
		Field field = JoinPointUtil.getFieldFor(thisJoinPointStaticPart);
		ITransaction transaction = currentTransaction(pojo);

		IDomainObject domainObject = pojo.domainObject();
		IDomainObject.IObjectOneToOneReference reference = null;
		if (domainObject.getPersistState() != PersistState.UNKNOWN) {
			reference = JoinPointUtil.getOneToOneReferenceFor(domainObject, thisJoinPointStaticPart);
		}
		IChange change = new OneToOneReferenceChange(transaction, pojo, field, referencedObjOrNull, reference);
		try {
			return change.execute();
		} finally {
			getLogger().debug("changingOneToOneReferenceOnPojo(pojo=" + pojo+"): end");
		}
	}


	
	@Override
	protected Logger getLogger() {
		return Logger.getLogger(TransactionOneToOneReferenceChangeAspectAdvice.class);
	}

}
