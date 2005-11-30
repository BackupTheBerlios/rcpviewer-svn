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

/**
 * Note that this aspect does <i>not</i> use the instantiatingPojo pointcut
 * from PojoAspect since it is too broad; rather it picks up on the creating 
 * of a pojo by the ISession.
 * 
 */
public aspect TransactionInstantiationChangeAspect extends TransactionAspect {

	private final static Logger LOG = Logger.getLogger(TransactionInstantiationChangeAspect.class);
	protected Logger getLogger() { return LOG; }

	pointcut creatingPersistentPojo(IPojo pojo):
		execution(private void ISession+.createdPersistent(IPojo+)) && args(pojo);
	
	pointcut creatingTransientPojo(IPojo pojo):
		execution(private void ISession+.createdTransient(IPojo+)) && args(pojo);
	
	pointcut recreatingPersistentPojo(IPojo pojo):
		execution(private void ISession+.createdTransient(IPojo+)) && args(pojo);
	
	pointcut creatingOrRecreatingPojo(IPojo pojo):
		creatingPersistentPojo(IPojo) && args(pojo) ||
		creatingTransientPojo(IPojo) && args(pojo) || 
		recreatingPersistentPojo(IPojo) && args(pojo);
	
	protected pointcut transactionalChange(IPojo pojo): 
		creatingOrRecreatingPojo(pojo) &&
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
	 * Creates an InstantiationChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied. 
	 */
	Object around(IPojo pojo): creatingOrRecreatingPojo(pojo) {
		ITransactable transactable = (ITransactable)pojo;
		ITransaction transaction = currentTransaction(transactable);
		IChange change = new InstantiationChange(transaction, transactable);

		return change.execute();
	}
	

}
