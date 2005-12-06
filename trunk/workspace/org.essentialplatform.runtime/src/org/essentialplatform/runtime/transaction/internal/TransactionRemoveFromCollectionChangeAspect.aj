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

public aspect TransactionRemoveFromCollectionChangeAspect extends TransactionAspect {
	
	private final static Logger LOG = Logger.getLogger(TransactionRemoveFromCollectionChangeAspect.class);
	protected Logger getLogger() { return LOG; }

	private TransactionRemoveFromCollectionChangeAspectAdvice advice = 
		new TransactionRemoveFromCollectionChangeAspectAdvice();

	protected pointcut transactionalChange(IPojo pojo):  
		this(pojo) &&  
		removingFromCollectionOnPojo(IPojo, Collection, Object) && 
		!within(TransactionCollectionChangeAspect) &&
		if(canBeEnlisted(pojo)) &&
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
	 * If we are able to locate the {@link org.essentialplatform.session.IDomainObject}
	 * wrapper for this pojo then get it to notify any listeners it has for
	 * this collection.
	 * 
	 * <p>
	 * In addition, notify all {@link IObservedFeature}s of the session.    
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
	 * Creates a RemoveFromCollectionChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied.
	 * 
	 * <p>
	 * TODO: how pick up the collection name?
	 */
	Object around(IPojo pojo, Collection collection, Object removedObj): 
			transactionalRemovingFromCollectionOnPojo(pojo, collection, removedObj) {
		return advice.around$transactionalRemovingFromCollectionOnPojo(pojo, collection, removedObj);
	}

	


}
