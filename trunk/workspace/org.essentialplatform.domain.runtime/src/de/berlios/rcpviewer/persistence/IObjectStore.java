package de.berlios.rcpviewer.persistence;

import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;

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

}
