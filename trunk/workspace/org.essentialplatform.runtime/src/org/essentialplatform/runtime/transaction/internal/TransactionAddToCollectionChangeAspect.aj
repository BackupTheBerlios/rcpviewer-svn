package org.essentialplatform.runtime.transaction.internal;

import java.util.Collection;

import org.apache.log4j.Logger;

import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.transaction.*;
import org.essentialplatform.runtime.transaction.changes.*;
import org.essentialplatform.runtime.persistence.IPersistable;
import org.essentialplatform.runtime.persistence.IPersistable.PersistState;

import java.util.concurrent.Callable;

public aspect TransactionAddToCollectionChangeAspect extends TransactionAspect {
	
	private final static Logger LOG = Logger.getLogger(TransactionAddToCollectionChangeAspect.class);
	protected Logger getLogger() { return LOG; }
	
	private TransactionAddToCollectionChangeAspectAdvice advice = 
		new TransactionAddToCollectionChangeAspectAdvice();

	
	// as required by super-aspect
	protected pointcut transactionalChange(IPojo pojo): 
		this(pojo) &&
		addingToCollectionOnPojo(IPojo, Collection, Object) && 
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
	 * Creates an AddToCollectionChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied.
	 *  
	 */
	Object around(IPojo pojo, Collection collection, Object addedObj): 
			transactionalAddingToCollectionOnPojo(pojo, collection, addedObj) {
		return advice.around$transactionalAddingToCollectionOnPojo(pojo, collection, addedObj);
	}

}
