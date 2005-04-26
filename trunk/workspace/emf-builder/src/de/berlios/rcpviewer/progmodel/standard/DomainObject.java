package de.berlios.rcpviewer.progmodel.standard;

import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;

/**
 * Wrapper for a POJO that also knows its {@link IDomainClass}.
 * 
 * <p>
 * Implementation note: created by {@link DomainAspect} (perthis aspect for 
 * pojos).
 * 
 * @author Dan Haywood
 */
public final class DomainObject implements IDomainObject {

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
