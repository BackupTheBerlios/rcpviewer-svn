package org.essentialplatform.runtime.shared.transaction.internal;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IObservedFeature;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.session.ISession;
import org.essentialplatform.runtime.shared.transaction.ITransactable;
import org.essentialplatform.runtime.shared.transaction.ITransaction;
import org.essentialplatform.runtime.shared.transaction.changes.AttributeChange;
import org.essentialplatform.runtime.shared.transaction.changes.IChange;
import org.essentialplatform.runtime.shared.domain.PojoAspect;

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
