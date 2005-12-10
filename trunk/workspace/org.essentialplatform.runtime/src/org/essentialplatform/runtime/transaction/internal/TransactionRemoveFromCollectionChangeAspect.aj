package org.essentialplatform.runtime.transaction.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.aspectj.lang.JoinPoint;

import org.apache.log4j.Logger;

import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.domain.IDomainObject;

import org.essentialplatform.runtime.transaction.*;
import org.essentialplatform.runtime.transaction.changes.*;

import org.essentialplatform.runtime.persistence.IPersistable;
import org.essentialplatform.runtime.persistence.IPersistable.PersistState;
import java.util.concurrent.Callable;
import org.essentialplatform.runtime.domain.PojoAspect;
public aspect TransactionRemoveFromCollectionChangeAspect extends PojoAspect {
	
	private final static Logger LOG = Logger.getLogger(TransactionRemoveFromCollectionChangeAspect.class);
	protected Logger getLogger() { return LOG; }

	private TransactionRemoveFromCollectionChangeAspectAdvice advice = 
		new TransactionRemoveFromCollectionChangeAspectAdvice();

//	protected pointcut transactionalChange(IPojo pojo):  
//		this(pojo) &&  
//		removingFromCollectionOnPojo(IPojo, Collection, Object) && 
//		!within(TransactionCollectionChangeAspect) &&
//		if(canBeEnlisted(pojo)) &&
//		!cflowbelow(invokeOperationOnPojo(IPojo)) ; 
//
//	
//	/**
//	 * Obtains transaction from either the thread or from the pojo (checking
//	 * that they don't conflict).
//	 * 
//	 * <p>
//	 * This code is identical in all subaspects of TransactionChange, however
//	 * moving it up and declaring a precedence doesn't seem to do the trick.
//	 */
//	Object around(final IPojo pojo): transactionalChange(pojo) {
//		return advice.around$transactionalChange(
//				pojo, 
//				new Callable() { 
//					public Object call() {
//						return proceed(pojo);
//					}
//				});
//	}



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
