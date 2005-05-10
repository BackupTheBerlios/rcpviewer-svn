package de.berlios.rcpviewer.session;

import java.util.List;

import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.progmodel.standard.impl.Department;

/**
 * Holds the collection of pojos (wrapped in {@link IDomainObject}s known to
 * the current user, cf a Hibernate Session.
 * 
 * <p>
 * 
 * <p>
 * Notes: the intention is that this object will be long-lived in a two-tier
 * (client/server) deployment, but short-lived in a three-tier (app server)
 * deployment.
 * 
 * @author Dan Haywood
 */
public interface ISession {

	/**
	 * Injected service that provides the ability to wrap a pojo in an
	 * {@link IDomainObject}.
	 * 
	 * @return
	 */
	IWrapper getWrapper();
	
	/**
	 * Creates a new pojo wrapped in an {@link IDomainObject}, and automatically
	 * attaches to the session.
	 * 
	 * <p>
	 * TODO: forgive my ignorance: is there a way of templating this so that it
	 * returns a <T> of same type as supplied IDomainClass?
	 * 
	 * @param domainClass
	 * @return an {@link IDomainObject} wrapping a newly created pojo.
	 */
	<T> IDomainObject<T> createTransient(IDomainClass<T> domainClass);

	/**
	 * Whether the supplied pojo is attached to this session.
	 * 
	 * @param pojo
	 * @return true if attached, false otherwise.
	 */
	boolean isAttached(Object pojo);
	
	/**
	 * Whether the pojo wrapped in the supplied {@link IDomainObject} is 
	 * attached to this session.
	 * 
	 * @param pojo
	 * @return true if attached, false otherwise.
	 */
	boolean isAttached(IDomainObject<?> domainObject);
	
	/**
	 * Detach the supplied pojo from the session.
	 * 
	 * @param pojo
	 * @throws IllegalArgumentException if the pojo was not attached.
	 */
	void detach(Object pojo);
	
	/**
	 * Detach the pojo wrapped in the supplied domainObject from the session.
	 * 
	 * @param domainObject
	 * @throws IllegalArgumentException if the pojo was not attached.
	 */
	void detach(IDomainObject<?> domainObject);
	
	/**
	 * Persist this object to the configured object store.
	 * 
	 * @param domainObject
	 */
	void persist(IDomainObject<?> domainObject);
	
	/**
	 * Detach all instances.
	 * 
	 * <p>
	 * Primarily of use for testing.
	 */
	void reset();

	/**
	 * Attach the pojo wrapped in the supplied domainObject to the session.
	 * 
	 * @param domainObject
	 * @throws IllegalArgumentException if the pojo was already attached.
	 */
	void attach(Department pojo);

	/**
	 * Attach the pojo wrapped in the supplied domainObject to the session.
	 * 
	 * @param domainObject
	 * @throws IllegalArgumentException if the pojo was already attached.
	 */
	void attach(IDomainObject<?> domainObject);

	/**
	 * Returns all attached objects of the supplied class.
	 * 
	 * @param domainClass
	 * @return
	 */
	List<IDomainObject<?>> footprintFor(IDomainClass<?> domainClass);

}
