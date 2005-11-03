package org.essentialplatform.core.domain.adapters;

import org.essentialplatform.core.domain.IDomainClass;

public abstract class AbstractDomainClassAdapter implements IDomainClassAdapter {

	private IDomainClass adaptedDomainClass;

	public AbstractDomainClassAdapter(final IDomainClass domainClass) {
		this.adaptedDomainClass = domainClass;
	}

	public IDomainClass adapts() {
		return adaptedDomainClass;
	}

}
