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
import org.essentialplatform.runtime.util.AspectsUtil;

class TransactionAddToCollectionChangeAspectAdvice extends TransactionAspectAdvice {


	/**
	 * Obtains transaction from either the thread or from the pojo (checking
	 * that they don't conflict).
	 * 
	 * <p>
	 * This code is identical in all subaspects of TransactionChange, however
	 * moving it up and declaring a precedence doesn't seem to do the trick.
	 */
	Object around$transactionalChange(IPojo pojo, Callable proceed ) {
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
	 * If we are able to locate the {@link org.essentialplatform.session.IDomainObject}
	 * wrapper for this pojo then get it to notify any listeners it has for
	 * this collection.
	 * 
	 * <p>
	 * In addition, notify all {@link IObservedFeature}s of the session.
	 */
	Object around$invokeAddToCollectionOnPojo(
			IPojo pojo, IPojo addedObject, JoinPoint.StaticPart thisJoinPointStaticPart, Callable proceed) {
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject.getPersistState() == PersistState.UNKNOWN) {
			return call(proceed);
		} else {
			IDomainObject.IObjectCollectionReference reference = 
				AspectsUtil.getCollectionReferenceFor(domainObject, thisJoinPointStaticPart);
			
			ThreadLocals.setCollectionReferenceForThread(reference);
			try {
				return call(proceed);
			} finally {
				ThreadLocals.clearCollectionReferenceForThread();
			}
		}
	}

	
	
	/**
	 * Creates an AddToCollectionChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied.
	 *  
	 * <p>
	 * Note the trick for picking up the collection name: from ThreadLocals.
	 */
	Object around$transactionalAddingToCollectionOnPojo(
			IPojo pojo, Collection collection, Object addedObj) {
		ITransactable transactable = (ITransactable)pojo;
		ITransaction transaction = currentTransaction(transactable);
		IChange change = new AddToCollectionChange(transaction, transactable, collection, addedObj, ThreadLocals.getCollectionReferenceForThreadIfAny());
		return change.execute();
	}

	@Override
	protected Logger getLogger() {
		return Logger.getLogger(TransactionAddToCollectionChangeAspectAdvice.class);
	}


}
