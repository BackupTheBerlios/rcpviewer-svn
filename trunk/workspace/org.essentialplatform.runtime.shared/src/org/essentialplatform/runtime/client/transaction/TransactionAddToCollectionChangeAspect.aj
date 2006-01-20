package org.essentialplatform.runtime.client.transaction;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.client.domain.InteractionsAspect;

public aspect TransactionAddToCollectionChangeAspect extends InteractionsAspect {
	
	private final static Logger LOG = Logger.getLogger(TransactionAddToCollectionChangeAspect.class);
	protected Logger getLogger() { return LOG; }
	
	private TransactionAddToCollectionChangeAspectAdvice advice = 
		new TransactionAddToCollectionChangeAspectAdvice();

	
	/**
	 * @see org.essentialplatform.runtime.transaction.internal.TransactionAddToCollectionChangeAspectAdvice#around$invokeAddToCollectionOnPojo(IPojo, IPojo, JoinPoint.StaticPart, Callable)
	 */
	Object around(final IPojo pojo, final IPojo addedObject): invokeAddToCollectionOnPojo(pojo, addedObject) {
		return advice.around$invokeAddToCollectionOnPojo(
				pojo, addedObject, thisJoinPointStaticPart, 
				new Callable() {
					public Object call() {
						return proceed(pojo, addedObject);
					}
				});
	}

	
	
	/**
	 * @see org.essentialplatform.runtime.transaction.internal.TransactionAddToCollectionChangeAspectAdvice#around$addingToCollectionOnPojo(IPojo, Collection, Object)
	 */
	Object around(IPojo pojo, Collection collection, Object addedObj): 
			addingToCollectionOnPojo(pojo, collection, addedObj) {
		return advice.around$addingToCollectionOnPojo(pojo, collection, addedObj);
	}

}
