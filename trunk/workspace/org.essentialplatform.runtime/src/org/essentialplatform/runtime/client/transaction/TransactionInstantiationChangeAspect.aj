package org.essentialplatform.runtime.client.transaction;

import java.util.concurrent.Callable;

import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.client.domain.InteractionsAspect;


/**
 * Note that this aspect does <i>not</i> use the instantiatingPojo pointcut
 * from InteractionsAspect since it is too broad; rather it picks up on the 
 * creating of a pojo by the {@link IClientSession}.
 * 
 */
public aspect TransactionInstantiationChangeAspect extends InteractionsAspect {

	private TransactionInstantiationChangeAspectAdvice advice = 
		new TransactionInstantiationChangeAspectAdvice();

	pointcut creatingPersistentPojo(IPojo pojo):
		execution(private void IClientSession+.createdPersistent(IPojo+)) && args(pojo);
	
	pointcut creatingTransientPojo(IPojo pojo):
		execution(private void IClientSession+.createdTransient(IPojo+)) && args(pojo);
	
	pointcut recreatingPersistentPojo(IPojo pojo):
		execution(private void IClientSession+.createdTransient(IPojo+)) && args(pojo);
	
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
