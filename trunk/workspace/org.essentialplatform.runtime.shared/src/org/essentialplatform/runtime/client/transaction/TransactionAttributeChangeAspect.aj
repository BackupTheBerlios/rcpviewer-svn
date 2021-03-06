package org.essentialplatform.runtime.client.transaction;

import java.util.concurrent.Callable;

import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.PojoAspect;
import org.essentialplatform.runtime.shared.domain.InteractionsAspect;

/**
 * One change per modified attribute performed directly (ie not programmatically
 * from an invoked operation).
 */
public aspect TransactionAttributeChangeAspect extends InteractionsAspect {

	private TransactionAttributeChangeAspectAdvice advice = 
		new TransactionAttributeChangeAspectAdvice();
	
	declare precedence: PojoAspect, TransactionAttributeChangeAspect; 

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
