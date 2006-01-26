package org.essentialplatform.runtime.server.persistence;

import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.ConcurrencyException;
import org.essentialplatform.runtime.shared.persistence.DuplicateObjectException;
import org.essentialplatform.runtime.shared.session.IObjectStoreHandle;

/**
 * Abstracts out mechanics of persisting, saving and deleting domain objects.
 * 
 * <p>
 * To explain some terminology:
 * <ul>
 * <li> <i>Saving</i> corresponds to <i>SQL INSERT</i>.
 * <li> <i>Updating</i> corresponds to <i>SQL UPDATE</i>.
 * <li> <i>Deleting</i> corresponds to <i>SQL DELETE</i>.
 * 
 * @author Dan Haywood
 *
 */
public interface IObjectStore {

	/**
	 * Unique identifier for this deployment of the object store.
	 * 
	 * <p>
	 * For example, <i>London</i> or <i>Prod-Env</i>.
	 * 
	 * @return
	 */
	public String getId();
	
	
	/**
	 * Persist this (currently transient) object.
	 * 
	 * @param domainObject 
	 * @throws <tt>DuplicateObjectException</tt> - if the object being persisted has 
	 *         the same unique identifier as another already persisted object
	 */
	public <T> void save(IDomainObject<T> domainObject) 
			throws DuplicateObjectException;

	/**
	 * Update this (already persistent) object.
	 * 
	 * <p>
	 * If the object
	 * 
	 * @param domainObject 
	 * @throws <tt>ConcurrencyException</tt> - if the object has already been updated.
	 * @throws <tt>DuplicateObjectException</tt> - if the object has been updated with 
	 *         the unique identifier of some other persisted object
	 */
	public <T> void update(IDomainObject<T> domainObject) 
			throws ConcurrencyException, DuplicateObjectException;

	/**
	 * Convenience method that will save (initially persist) a still-transient
	 * pojo, or will update an already persistent object.
	 * 
	 * <p>
	 * Modelled after Hibernate.
	 * 
	 * @see #save(IDomainObject)
	 * @see #update(IDomainObject)
	 * 
	 * @param domainObject
	 */
	public <T> void saveOrUpdate(IDomainObject<T> domainObject);

	/**
	 * Delete the object represented by the domain object.
	 * 
	 * @param domainObject
	 */
	public void delete(IDomainObject<?> domainObject);

	/**
	 * Whether the supplied domain object has been persisted in this 
	 * objectstore.
	 * 
	 * @param domainObject
	 * @return
	 */
	public <T> boolean isPersistent(IDomainObject<T> domainObject);

}
