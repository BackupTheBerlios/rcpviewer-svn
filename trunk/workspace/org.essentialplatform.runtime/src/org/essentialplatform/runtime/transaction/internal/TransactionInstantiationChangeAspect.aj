package org.essentialplatform.runtime.transaction.internal;

import org.apache.log4j.Logger;

import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.session.ISession;

import org.essentialplatform.runtime.transaction.*;
import org.essentialplatform.runtime.transaction.changes.*;

import java.util.concurrent.*;
import org.essentialplatform.runtime.domain.PojoAspect;
/**
 * Note that this aspect does <i>not</i> use the instantiatingPojo pointcut
 * from PojoAspect since it is too broad; rather it picks up on the creating 
 * of a pojo by the ISession.
 * 
 */
public aspect TransactionInstantiationChangeAspect extends PojoAspect {

	private final static Logger LOG = Logger.getLogger(TransactionInstantiationChangeAspect.class);
	protected Logger getLogger() { return LOG; }
	
	private TransactionInstantiationChangeAspectAdvice advice = 
		new TransactionInstantiationChangeAspectAdvice();

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
	Object around(final IPojo pojo): transactionalChange(pojo) {
		return advice.around$transactionalChange(
				pojo, 
				new Callable() {
					public Object call() {
						return proceed(pojo);
					}
				});
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
	Object around(final IPojo pojo): creatingOrRecreatingPojo(pojo) {
		return advice.around$creatingOrRecreatingPojo(pojo);
	}
	

}
