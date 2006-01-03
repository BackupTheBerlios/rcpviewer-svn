package org.essentialplatform.runtime.server.session;

import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.ConcurrencyException;
import org.essentialplatform.runtime.shared.persistence.DuplicateObjectException;

public interface IServerSession {

	public void attach(IDomainObject<?> domainObject);


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

	public void close();


}
