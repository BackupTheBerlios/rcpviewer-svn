package org.essentialplatform.runtime.transaction.internal;

import java.util.Collection;

import org.apache.log4j.Logger;

import org.essentialplatform.runtime.domain.PojoAspect;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.transaction.*;
import org.essentialplatform.runtime.transaction.changes.*;
import org.essentialplatform.runtime.persistence.IPersistable;
import org.essentialplatform.runtime.persistence.IPersistable.PersistState;

import java.util.concurrent.Callable;

public aspect TransactionAddToCollectionChangeAspect extends PojoAspect {
	
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
