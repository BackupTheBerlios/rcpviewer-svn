package org.essentialplatform.runtime.client.transaction;

import java.util.concurrent.Callable;

import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.PojoAspect;
import org.essentialplatform.runtime.shared.domain.InteractionsAspect;

/**
 * One change per modified 1:1 reference performed directly (ie not programmatically
 * from an invoked operation).
 */
public aspect TransactionOneToOneReferenceChangeAspect extends InteractionsAspect {
	
	private TransactionOneToOneReferenceChangeAspectAdvice advice = 
		new TransactionOneToOneReferenceChangeAspectAdvice();
	
	declare precedence: PojoAspect, TransactionOneToOneReferenceChangeAspect; 

	/**
	 * @see TransactionOneToOneReferenceChangeAspectAdvice
	 */
	Object around(final IPojo pojo, final IPojo newReferencedObject): invokeAssociatorForOneToOneReferenceOnPojo(pojo, newReferencedObject) {
		return advice.around$invokeAssociatorForOneToOneReferenceOnPojo(
				pojo,
				newReferencedObject,
				new Callable() {
					public Object call() {
						return proceed(pojo, newReferencedObject);
					}
				});
	}

	/**
	 * @see TransactionOneToOneReferenceChangeAspectAdvice
	 */
	Object around(final IPojo pojo, final IPojo existingReferencedObject): invokeDissociatorForOneToOneReferenceOnPojo(pojo, existingReferencedObject) {
		return advice.around$invokeDissociatorForOneToOneReferenceOnPojo(
			pojo,
			existingReferencedObject,
			new Callable() {
				public Object call() {
					return proceed(pojo, existingReferencedObject);
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
