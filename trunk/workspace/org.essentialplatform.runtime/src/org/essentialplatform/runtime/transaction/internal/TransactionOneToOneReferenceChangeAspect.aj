package org.essentialplatform.runtime.transaction.internal;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import org.essentialplatform.progmodel.essential.app.InDomain;

import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.PojoAspect;
import org.essentialplatform.runtime.domain.IObservedFeature;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.session.ISession;

import org.essentialplatform.runtime.transaction.*;
import org.essentialplatform.runtime.transaction.changes.*;

import org.essentialplatform.runtime.persistence.IPersistable;
import org.essentialplatform.runtime.persistence.IPersistable.PersistState;

import java.util.concurrent.*;
import org.essentialplatform.runtime.domain.PojoAspect;
/**
 * One change per modified 1:1 reference performed directly (ie not programmatically
 * from an invoked operation).
 */
public aspect TransactionOneToOneReferenceChangeAspect extends PojoAspect {
	
	private final static Logger LOG = Logger.getLogger(TransactionOneToOneReferenceChangeAspect.class);
	protected Logger getLogger() { return LOG; }
	private TransactionOneToOneReferenceChangeAspectAdvice advice = 
		new TransactionOneToOneReferenceChangeAspectAdvice();
	
	protected pointcut transactionalChange(IPojo pojo): 
		changingOneToOneReferenceOnPojo(pojo, Object) &&
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
	 * Creates an OneToOneReferenceChange to wrap a change to the attribute, adding it
	 * to the current transaction.
	 *  
	 * <p>
	 * This code must appear after the transactionChange() advice above 
	 * because lexical ordering is used to determine the order in which
	 * advices are applied. 
	 */
	Object around(IPojo pojo, IPojo referencedObjOrNull): 
			changingOneToOneReferenceOnPojo(pojo, referencedObjOrNull) {
		return advice.around$changingOneToOneReferenceOnPojo(
				pojo, referencedObjOrNull, thisJoinPointStaticPart);
	}

}
