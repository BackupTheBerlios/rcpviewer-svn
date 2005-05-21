package de.berlios.rcpviewer.session;

import java.util.List;

import de.berlios.rcpviewer.domain.IDomainClass;

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
	 * Creates a new pojo wrapped in an {@link IDomainObject}, and automatically
	 * attaches to the session.
	 * 
	 * @param domainClass
	 * @return an {@link IDomainObject} wrapping a newly created pojo.
	 */
	<T> IDomainObject<T> createTransient(IDomainClass<T> domainClass);

	/**
	 * Attach the pojo wrapped in the supplied domainObject to the session.
	 * 
	 * <p>
	 * Any {@link ISessionListener}s of the session will be notified that the
	 * {@link IDomainObject} has been attached.
	 * 
	 * @param domainObject
	 * @throws IllegalArgumentException if the pojo was already attached.
	 */
	void attach(IDomainObject<?> domainObject);

	
	/**
	 * Detach the pojo wrapped in the supplied domainObject from the session.
	 * 
	 * <p>
	 * Any {@link ISessionListener}s of the session will be notified that the
	 * {@link IDomainObject} has been detached.
	 * 
	 * @param domainObject
	 * @throws IllegalArgumentException if the pojo was not attached.
	 */
	void detach(IDomainObject<?> domainObject);

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
	 * Detach all instances.
	 * 
	 * <p>
	 * Note that <i>no</i> {@link IDomainObjectListener}s will be notified. 
	 * 
	 * <p>
	 * Primarily of use for testing.
	 */
	void reset();

	
	/**
	 * Persist this object to the configured object store.
	 * 
	 * <p>
	 * Should delegate to the {@link IDomainObject} to do the persist; 
	 * {@link IDomainObjectListener}s of the {@link IDomainObject} (<i>not</i>
	 * the session) will be notified.
	 *  
	 * @param domainObject
	 */
	void persist(IDomainObject<?> domainObject);
	

	/**
	 * Persist this object to the configured object store.
	 * 
	 * <p>
	 * Should delegate to the {@link IDomainObject} for the pojo to do the 
	 * persist; {@link IDomainObjectListener}s of the {@link IDomainObject} 
	 * (<i>not</i> the session) will be notified.
	 *  
	 * @param domainObject
	 */
	void persist(Object pojo);

	
	/**
	 * Returns all attached objects of the supplied class.
	 * 
	 * @param domainClass
	 * @return
	 */
	List<IDomainObject<?>> footprintFor(IDomainClass<?> domainClass);

	/**
	 * Adds session listener.
	 * 
	 * <p>
	 * If the listener is already known, does nothing.
	 * 
	 * <p>
	 * Note: we return the listener only because it slightly simplfies the
	 * implementation of tests.
	 * 
	 * @param listener
	 */
	public <T extends ISessionListener> T addSessionListener(T listener);

	
	/**
	 * Removes session listener.
	 * 
	 * <p>
	 * If the listener is unknown, does nothing.
	 * 
	 * @param listener
	 */
	public void removeSessionListener(ISessionListener listener);

	<T> IDomainObject<T> getDomainObjectFor(Object pojo, Class<T> pojoClass);

}
