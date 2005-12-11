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
	 * @see org.essentialplatform.runtime.transaction.internal.TransactionInstantiationChangeAspectAdvice#around$transactionalChange(IPojo, Callable)
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
	 * @see org.essentialplatform.runtime.transaction.internal.TransactionInstantiationChangeAspectAdvice#around$creatingOrRecreatingPojo(IPojo)
	 */
	Object around(final IPojo pojo): creatingOrRecreatingPojo(pojo) {
		return advice.around$creatingOrRecreatingPojo(pojo);
	}
	

}
