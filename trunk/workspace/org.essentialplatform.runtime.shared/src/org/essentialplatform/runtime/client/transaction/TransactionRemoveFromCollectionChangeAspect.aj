package org.essentialplatform.runtime.client.transaction;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.aspectj.lang.JoinPoint;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.PojoAspect;
import org.essentialplatform.runtime.shared.domain.InteractionsAspect;

public aspect TransactionRemoveFromCollectionChangeAspect extends InteractionsAspect {
	
	private TransactionRemoveFromCollectionChangeAspectAdvice advice = 
		new TransactionRemoveFromCollectionChangeAspectAdvice();

	declare precedence: PojoAspect, TransactionRemoveFromCollectionChangeAspect; 

	/**
	 * @see org.essentialplatform.runtime.transaction.internal.TransactionRemoveFromCollectionChangeAspectAdvice#around$invokeRemoveFromCollectionOnPojo(IPojo, Object, JoinPoint.StaticPart, Callable)
	 */
	Object around(final IPojo pojo, final Object removedObject): invokeRemoveFromCollectionOnPojo(pojo, removedObject) {
		return advice.around$invokeRemoveFromCollectionOnPojo(
				pojo, removedObject, thisJoinPointStaticPart,
				new Callable() { 
					public Object call() {
						return proceed(pojo, removedObject);
					}
				});
	}

	
	/**
	 * @see org.essentialplatform.runtime.transaction.internal.TransactionRemoveFromCollectionChangeAspectAdvice#around$removingFromCollectionOnPojo(IPojo, Collection, Object)
	 */
	Object around(IPojo pojo, Collection collection, Object removedObj): 
			removingFromCollectionOnPojo(pojo, collection, removedObj) {
		return advice.around$removingFromCollectionOnPojo(pojo, collection, removedObj);
	}

	


}
