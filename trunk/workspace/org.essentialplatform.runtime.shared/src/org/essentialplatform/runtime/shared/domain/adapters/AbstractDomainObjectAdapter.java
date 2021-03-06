package org.essentialplatform.runtime.shared.domain.adapters;

import org.essentialplatform.runtime.shared.domain.IDomainObject;

public abstract class AbstractDomainObjectAdapter<T> implements IDomainObjectAdapter<T> {

	public AbstractDomainObjectAdapter(IDomainObject<T> domainObject) {
		this.domainObject = domainObject;
	}


	private IDomainObject<T> domainObject;
	/**
	 * The object for which this is an adapter.
	 */
	public final IDomainObject<T> adapts() {
		return domainObject;
	}

}
