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
import org.essentialplatform.runtime.domain.PojoAspect;

/**
 * One change per modified attribute performed directly (ie not programmatically
 * from an invoked operation).
 */
public aspect TransactionAttributeChangeAspect extends PojoAspect {

	private TransactionAttributeChangeAspectAdvice advice = 
		new TransactionAttributeChangeAspectAdvice();
	

	/**
	 * @see org.essentialplatform.runtime.transaction.internal.TransactionOneToOneReferenceChangeAspectAdvice#
	 */
	Object around(final IPojo pojo, final Object postValue): invokeSetterForAttributeOnPojo(pojo, postValue) {
		return advice.around$invokeSetterForAttributeOnPojo(
			pojo, 
			postValue, 
			new Callable() {
				public Object call() { return proceed(pojo, postValue); }
			}
		);
	}

	/**
	 * @see TransactionAttributeChangeAspectAdvice#around$changingAttributeOnPojo(IPojo, Object, JoinPoint.StaticPart)
	 */
	Object around(IPojo pojo, Object postValue): 
			changingAttributeOnPojo(pojo, postValue) {
		return advice.around$changingAttributeOnPojo(pojo, postValue, thisJoinPointStaticPart);
	}

}
