package org.essentialplatform.runtime.shared.persistence;

import java.util.List;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.filters.IdAttributeFilter;
import org.essentialplatform.progmodel.essential.core.domain.comparators.IdAttributeComparator;
import org.essentialplatform.runtime.client.RuntimeClientBinding;
import org.essentialplatform.runtime.client.RuntimeClientBinding.RuntimeClientClassBinding;
import org.essentialplatform.runtime.shared.domain.IDomainObject;

public final class CompositeIdPersistenceIdAssigner extends AbstractPersistenceIdAssigner {

	private final IDomainClass _domainClass;
	private final Class<?> _javaClass;
	private List<IAttribute> _idAttributes; 

	public CompositeIdPersistenceIdAssigner(IDomainClass domainClass) {
		this(domainClass, null);
	}

	/**
	 * 
	 * @param domainClass
	 * @param nextAssigner
	 */
	public CompositeIdPersistenceIdAssigner(IDomainClass domainClass, IPersistenceIdAssigner nextAssigner) {
		super(nextAssigner);
		_domainClass = domainClass;
		_javaClass = ((RuntimeClientClassBinding)domainClass.getBinding()).getJavaClass();
	}

	/*
	 * @see org.essentialplatform.runtime.persistence.AbstractPersistenceIdAssigner#doAssignPersistenceIdFor(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	@Override
	protected <T> PersistenceId doGeneratePersistenceIdFor(IDomainObject<T> domainObject) {
		assert _domainClass == domainObject.getDomainClass();
		cacheIdAttributesIfRequired();
		PersistenceId persistenceId = new PersistenceId(_javaClass);
		for(IAttribute idAttribute: _idAttributes) {
			Object attributeValue = domainObject.getAttribute(idAttribute).get();
			persistenceId.addComponentValue(attributeValue);
		}
		return persistenceId;
	}

	private synchronized void cacheIdAttributesIfRequired() {
		if (_idAttributes == null) {
			_idAttributes = _domainClass.iAttributes(new IdAttributeFilter(), new IdAttributeComparator());
			if (_idAttributes.size() == 0) {
				throw new IllegalArgumentException("No Id attributes");
			}
		}
	}

}
