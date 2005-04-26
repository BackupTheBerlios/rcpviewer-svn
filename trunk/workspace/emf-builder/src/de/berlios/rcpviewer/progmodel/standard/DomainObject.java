package de.berlios.rcpviewer.progmodel.standard;

import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;

/**
 * Wrapper for a POJO that also knows its {@link IDomainClass}.
 * 
 * <p>
 * Implementation note: created by {@link DomainAspect} (perthis aspect for 
 * pojos).
 * 
 * @author Dan Haywood
 */
public final class DomainObject<T> implements IDomainObject {

	public DomainObject(final IDomainClass domainClass, final T pojo) {
		this.domainClass = domainClass;
		this.pojo = pojo;
	}
	
	private final IDomainClass domainClass;
	public IDomainClass getDomainClass() {
		return domainClass;
	}
	
	private final T pojo;
	public T getPojo() {
		return pojo;
	}

	private boolean persistent;
	public boolean isPersistent() {
		return persistent;
	}

	public void persist() {
		if (isPersistent()) {
			throw new IllegalStateException("Already persisted.");
		}
		InMemoryObjectStore.instance().persist(this.title(), this);
		persistent = true;
	}
	
	/**
	 * For the title we just return the toString.
	 */
	public String title() {
		return pojo.toString();
	}

}
