package de.berlios.rcpviewer.domain;

public abstract class AbstractDomainClassAdapter implements IDomainClassAdapter {

	private IDomainClass adaptedDomainClass;

	public AbstractDomainClassAdapter(final IDomainClass domainClass) {
		this.adaptedDomainClass = domainClass;
	}

	public IDomainClass adapts() {
		return adaptedDomainClass;
	}

}
