package de.berlios.rcpviewer.persistence;

import de.berlios.rcpviewer.metamodel.IDomainObject;
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
	 * Persist this object such that it may be retrieved at a later date.
	 * 
	 * 
	 * @param domainObject
	 */
	public void persist(String title, Object pojo);

	Object findByTitle(Class<?> javaClass, String title);

	/**
	 * For testing purposes.
	 *
	 */
	public void reset(); 
	
}
