package de.berlios.rcpviewer.persistence;

import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;

/**
 * Abstracts out mechanics of persisting pojos.
 * 
 * <p>
 * Note that this interface deals only in terms of pojos (vanilla 
 * java.lang.Objects) rather than IDomainObjects.  Callers that require an
 * IDomainObject should use the services of the {@link ISession}.
 * 
 * @author Dan Haywood
 *
 */
public interface IObjectStore {

	/**
	 * Persist this (currently transient) object such that it may be retrieved 
	 * at a later date.
	 * 
	 * @param title
	 * @param pojo 
	 */
	public <T> void persist(IDomainObject<T> domainObject);

	/**
	 * Save changes to this (already persistent) object such that it may be 
	 * retrieved at a later date.
	 * 
	 * 
	 * @param domainObject 
	 */
	public <T> void save(IDomainObject<T> domainObject);

	<V> V findByTitle(Class<V> javaClass, String title);
	
	/**
	 * For testing purposes.
	 *
	 */
	public void reset(); 
	
}
