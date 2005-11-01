package org.essentialplatform.transaction.internal;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import org.essentialplatform.progmodel.standard.InDomain;

import org.essentialplatform.session.IDomainObject;
import org.essentialplatform.session.PojoAspect;
import org.essentialplatform.session.ISession;
import org.essentialplatform.session.ISession;
import org.essentialplatform.session.IObservedFeature;
import org.essentialplatform.session.IPojo;

import org.essentialplatform.transaction.IChange;
import org.essentialplatform.transaction.PojoAlreadyEnlistedException;
import org.essentialplatform.transaction.ITransactable;
import org.essentialplatform.transaction.ITransaction;
import org.essentialplatform.transaction.ITransactionManager;

/**
 * One change per modified attribute performed directly (ie not programmatically
 * from an invoked operation).
 * 
 * <p>
 * Note that this aspect does <i>not</i> use the instantiatingPojo pointcut
 * from PojoAspect since it is too broad; rather it picks up on the creating 
 * of a pojo by the ISession.
 * 
 */
public aspect TransactionInstantiationChangeAspect extends TransactionChangeAspect {

	private final static Logger LOG = Logger.getLogger(TransactionInstantiationChangeAspect.class);
	protected Logger getLogger() { return LOG; }

	pointcut creatingPersistentPojo(IPojo pojo):
		execution(private void ISession+.createdPersistent(IPojo+)) && args(pojo);
	
	protected pointcut changingPojo(IPojo pojo): creatingPersistentPojo(pojo);
	protected pointcut transactionalChange(IPojo pojo): 
		changingPojo(pojo) &&
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

	/**
	 * Creates an InstantiationChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied. 
	 */
	Object around(IPojo pojo): creatingPersistentPojo(pojo) {
		ITransactable transactable = (ITransactable)pojo;
		ITransaction transaction = currentTransaction(transactable);
		IChange change = new InstantiationChange(transaction, transactable);

//		IDomainObject<?> domainObject = pojo.getDomainObject();
//		// only if we have a domain object (ie fully instantiated) and
//		// are attached to a session do we check.
//		if (domainObject != null && domainObject.isAttached()) {
//			if (!transaction.addingToInteractionChangeSet(change)) {
//				throw new PojoAlreadyEnlistedException();			
//			}
//		}
//		
		return change.execute();
	}
	

}
