package org.essentialplatform.runtime.shared.domain.handle;

import java.util.List;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.filters.IdAttributeFilter;
import org.essentialplatform.progmodel.essential.core.domain.comparators.IdAttributeComparator;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;

public final class CompositeIdHandleAssigner extends AbstractHandleAssigner {

	private final IDomainClass _domainClass;
	private final Class<?> _javaClass;
	private List<IAttribute> _idAttributes; 

	public CompositeIdHandleAssigner(IDomainClass domainClass) {
		this(domainClass, null);
	}

	/**
	 * 
	 * @param domainClass
	 * @param nextAssigner
	 */
	public CompositeIdHandleAssigner(IDomainClass domainClass, IHandleAssigner nextAssigner) {
		super(nextAssigner);
		_domainClass = domainClass;
		_javaClass = ((IDomainClassRuntimeBinding)domainClass.getBinding()).getJavaClass();
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.AbstractHandleAssigner#doGenerateHandleFor(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	@Override
	protected <T> Handle doGenerateHandleFor(IDomainObject<T> domainObject) {
		assert _domainClass == domainObject.getDomainClass();
		cacheIdAttributesIfRequired();
		Handle handle = new Handle(_javaClass);
		for(IAttribute idAttribute: _idAttributes) {
			Object attributeValue = domainObject.getAttribute(idAttribute).get();
			handle.addComponentValue(attributeValue);
		}
		return handle;
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
