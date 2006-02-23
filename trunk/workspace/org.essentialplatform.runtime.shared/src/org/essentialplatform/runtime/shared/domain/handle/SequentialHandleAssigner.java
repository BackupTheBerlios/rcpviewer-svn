package org.essentialplatform.runtime.shared.domain.handle;

import java.util.HashMap;
import java.util.Map;

import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;

/**
 * Generates {@link Handle}s sequentially with respect to the class of 
 * the domain object supplied (sequential with respect to the JVM on which
 * the call is made, that is).
 * 
 * <p>
 * For example, passing in a domain objects for a Customer and then a Product
 * will both obtain a {@link Handle} representing the number 1.  
 * If we pass in another Customer then it will obtain a {@link Handle}
 * representing the number 2.  And so on.   
 * 
 * @author Dan Haywood
 */
public final class SequentialHandleAssigner extends AbstractHandleAssigner {

	/**
	 * Map of the last number assigned for each class.
	 */
	private Map<Class<?>, int[]> _lastNumberByClass = 
		new HashMap<Class<?>, int[]>();
	
	public SequentialHandleAssigner() {
		super();
	}

	public SequentialHandleAssigner(IHandleAssigner nextAssigner) {
		super(nextAssigner);
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.AbstractHandleAssigner#doGenerateHandleFor(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	@Override
	protected <T> Handle doGenerateHandleFor(IDomainObject<T> domainObject) {
		IDomainClassRuntimeBinding binding = (IDomainClassRuntimeBinding)domainObject.getDomainClass().getBinding();
		Class<?> objectClass = binding.getJavaClass();
		int[] lastNumber = null;
		synchronized(_lastNumberByClass) {
			lastNumber = _lastNumberByClass.get(objectClass);
			if (lastNumber == null) {
				lastNumber = new int[]{0};
				_lastNumberByClass.put(objectClass, lastNumber);
			}
			lastNumber[0]++;
		}
		return new Handle(objectClass, new Integer(lastNumber[0])).updateAbbreviation(domainObject.getDomainClass().getAbbreviation());
	}

}
