package org.essentialplatform.runtime.transaction.internal;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IObservedFeature;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.session.ISession;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.changes.AttributeChange;
import org.essentialplatform.runtime.transaction.changes.IChange;


/**
 * One change per modified attribute performed directly (ie not programmatically
 * from an invoked operation).
 */
public aspect TransactionAttributeChangeAspect extends TransactionAspect {

	private TransactionAttributeChangeAspectAdvice advice = 
		new TransactionAttributeChangeAspectAdvice();
	
	protected pointcut transactionalChange(IPojo pojo): 
		transactionalChangingAttributeOnPojo(IPojo, Object) && 
		this(pojo) && 
		if(canBeEnlisted(pojo)) &&
		!cflowbelow(invokeOperationOnPojo(IPojo)) ;  // this is probably unnecessary since the invokeOperation aspect has precedence over this one...

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
				public Object call() { return proceed(pojo); }
			}
		);
	}

	/**
	 * Creates an AttributeChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * In addition, passes the IDomainObject's IAttribute to the change so that
	 * it can notify listeners as it is executed/undone.  
	 *  
	 * <p>
	 * The change also notifies all {@link IObservedFeature}s of the session.  
	 * That's because a prerequisite of an operation or an attribute might 
	 * become satisfied (or no longer satisfied) as a result of this change.
	 * 
	 * <p>
	 * <n>Implementation notes</n>: informing all observed features seems rather
	 * crude.  An alternative design and possibly preferable approach would be 
	 * to wait until the current "workgroup" (as defined by the transaction 
	 * aspect) has completed.
	 *    
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied. 
	 */
	Object around(IPojo pojo, Object postValue): 
			transactionalChangingAttributeOnPojo(pojo, postValue) {
		return advice.around$transactionalChangingAttributeOnPojo(pojo, postValue, thisJoinPointStaticPart);
	}

}
