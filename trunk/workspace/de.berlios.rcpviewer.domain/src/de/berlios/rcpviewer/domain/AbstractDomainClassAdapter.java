package de.berlios.rcpviewer.domain;

public class AbstractDomainClassAdapter<T> implements IDomainClassAdapter<T> {

	private IDomainClass<T> adaptedDomainClass;

	public AbstractDomainClassAdapter(final IDomainClass<T> domainClass) {
		this.adaptedDomainClass = domainClass;
	}

	public IDomainClass<T> adapts() {
		return adaptedDomainClass;
	}

}
