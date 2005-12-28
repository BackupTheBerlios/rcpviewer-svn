package org.essentialplatform.runtime.shared.transaction.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.aspectj.lang.JoinPoint;

import org.apache.log4j.Logger;

import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.PojoAspect;
import org.essentialplatform.runtime.shared.transaction.*;
import org.essentialplatform.runtime.shared.transaction.changes.*;
import org.essentialplatform.runtime.shared.persistence.IPersistable;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;

public aspect TransactionRemoveFromCollectionChangeAspect extends PojoAspect {
	
	private TransactionRemoveFromCollectionChangeAspectAdvice advice = 
		new TransactionRemoveFromCollectionChangeAspectAdvice();

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
