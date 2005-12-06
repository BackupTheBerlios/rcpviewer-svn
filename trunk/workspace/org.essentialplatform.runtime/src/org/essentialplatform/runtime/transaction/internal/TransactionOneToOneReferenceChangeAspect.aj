package org.essentialplatform.runtime.transaction.internal;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import org.essentialplatform.progmodel.essential.app.InDomain;

import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.PojoAspect;
import org.essentialplatform.runtime.domain.IObservedFeature;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.session.ISession;

import org.essentialplatform.runtime.transaction.*;
import org.essentialplatform.runtime.transaction.changes.*;

import org.essentialplatform.runtime.persistence.IPersistable;
import org.essentialplatform.runtime.persistence.IPersistable.PersistState;


/**
 * One change per modified 1:1 reference performed directly (ie not programmatically
 * from an invoked operation).
 */
public aspect TransactionOneToOneReferenceChangeAspect extends TransactionAspect {
	
	private final static Logger LOG = Logger.getLogger(TransactionOneToOneReferenceChangeAspect.class);
	protected Logger getLogger() { return LOG; }

	protected pointcut transactionalChange(IPojo pojo): 
		transactionalChangingOneToOneReferenceOnPojo(pojo, Object) &&
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
	Object around(IPojo pojo, IPojo referencedObjOrNull): 
			transactionalChangingOneToOneReferenceOnPojo(pojo, referencedObjOrNull) {
		getLogger().debug("transactionalChangingOneToOneReferenceOnPojo(pojo=" + pojo+")");
		Field field = getFieldFor(thisJoinPointStaticPart);
		ITransactable transactable = (ITransactable)pojo;
		ITransaction transaction = currentTransaction(transactable);

		IDomainObject domainObject = pojo.getDomainObject();
		IDomainObject.IObjectOneToOneReference reference = null;
		if (domainObject.getPersistState() != PersistState.UNKNOWN) {
			reference = getOneToOneReferenceFor(domainObject, thisJoinPointStaticPart);
		}
		IChange change = new OneToOneReferenceChange(transaction, transactable, field, referencedObjOrNull, reference);
		
		return change.execute();
	}

}
