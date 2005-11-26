package org.essentialplatform.runtime.persistence;

import java.util.HashMap;
import java.util.Map;

import org.essentialplatform.core.deployment.Binding.IClassBinding;
import org.essentialplatform.runtime.RuntimeBinding.RuntimeClassBinding;
import org.essentialplatform.runtime.domain.IDomainObject;

/**
 * Generates {@link PersistenceId}s sequentially with respect to the class of 
 * the domain object supplied.
 * 
 * <p>
 * For example, passing in a domain objects for a Customer and then a Product
 * will both obtain a {@link PersistenceId} representing the number 1.  
 * If we pass in another Customer then it will obtain a {@link PersistenceId}
 * representing the number 2.  And so on.   
 * 
 * @author Dan Haywood
 */
public final class SequentialPersistenceIdAssigner extends
		AbstractPersistenceIdAssigner {

	/**
	 * Map of the last number assigned for each class.
	 */
	private Map<Class<?>, int[]> _lastNumberByClass = 
		new HashMap<Class<?>, int[]>();
	
	public SequentialPersistenceIdAssigner() {
		super();
	}

	public SequentialPersistenceIdAssigner(IPersistenceIdAssigner nextAssigner) {
		super(nextAssigner);
	}

	/*
	 * @see org.essentialplatform.runtime.persistence.AbstractPersistenceIdAssigner#doAssignPersistenceIdFor(org.essentialplatform.runtime.domain.IDomainObject)
	 */
	@Override
	protected <T> PersistenceId doGeneratePersistenceIdFor(IDomainObject<T> domainObject) {
		RuntimeClassBinding binding = (RuntimeClassBinding)domainObject.getDomainClass().getBinding();
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
		return new PersistenceId(objectClass, new Integer(lastNumber[0]));
	}

}
