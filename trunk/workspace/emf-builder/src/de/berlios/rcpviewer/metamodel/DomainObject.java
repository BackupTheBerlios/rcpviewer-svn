package de.berlios.rcpviewer.metamodel;

/**
 * Wrapper for a POJO that also knows its {@link IDomainClass}.
 * 
 * <p>
 * Implementation note: created by {@link DomainAspect} (perthis aspect for 
 * pojos).
 */
public class DomainObject implements IDomainObject {

	public DomainObject(final IDomainClass domainClass, final Object pojo) {
		this.domainClass = domainClass;
		this.pojo = pojo;
	}
	
	private final IDomainClass domainClass;
	public IDomainClass getDomainClass() {
		return domainClass;
	}
	
	private final Object pojo;
	public Object getPojo() {
		return pojo;
	}

}
