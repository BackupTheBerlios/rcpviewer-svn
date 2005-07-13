package de.berlios.rcpviewer.session;

import java.util.List;

import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.persistence.IObjectStore;

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
	 * The unique identifier for this session.
	 * 
	 * <p>
	 * Typically there will be precisely one session per {@link IDomain}.
	 * However, the design allows multiple such sessions. 
	 * 
	 * <p>
	 * TODO: should make this a guid.
	 * 
	 * @return
	 */
	public String getId();
	
	/**
	 * The {@link RuntimeDomain} (or schema, or metamodel) that holds the
	 * {@link IDomainClass}es for which every {@link IDomainObject} managed
	 * by this session must correspond.
	 *   
	 * @return
	 */
	IDomain getDomain();
	
	/**
	 * The objectstore to which all {@link IDomainObject}s managed by this
	 * session will be persisted.
	 * 
	 * @return
	 */
	IObjectStore getObjectStore();
	
	/**
	 * Creates a new pojo wrapped in an {@link IDomainObject}, and automatically
	 * attaches to the session.
	 * 
	 * @param domainClass
	 * @return an {@link IDomainObject} wrapping a newly created pojo.
	 */
	<T> IDomainObject<T> createTransient(IRuntimeDomainClass<T> domainClass);

	/**
	 * Attach the pojo wrapped in the supplied {@link IDomainObject} to the 
	 * session.
	 * 
	 * <p>
	 * The id of the domain object must match that of the session itself.
	 * 
	 * <p>
	 * Any {@link ISessionListener}s of the session will be notified that the
	 * {@link IDomainObject} has been attached.
	 * 
	 * @param domainObject
	 * @throws IllegalStateException if the object was already attached.
	 * @throws IllegalArgumentException if the session does not match.
	 */
	<T> void attach(IDomainObject<T> domainObject);

	
	/**
	 * Detach the pojo wrapped in the supplied domainObject from the session.
	 * 
	 * <p>
	 * Any {@link ISessionListener}s of the session will be notified that the
	 * {@link IDomainObject} has been detached.
	 * 
	 * @param domainObject
	 * @throws IllegalStateException if the pojo was not attached.
	 */
	<T> void detach(IDomainObject<T> domainObject);

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
	<T> boolean isAttached(IDomainObject<T> domainObject);
	
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
	 * Persist this (currently transient) object to the configured object store.
	 * 
	 * <p>
	 * {@link IDomainObjectListener}s of the {@link IDomainObject} (<i>not</i>
	 * the session) will be notified.
	 *  
	 * @param domainObject
	 */
	<T> void persist(IDomainObject<T> domainObject);
	

	/**
	 * Persist this (currently transient) object to the configured object store.
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
	 * Save changes to this (already persistent) object to the
	 * configured object store.
	 * 
	 * <p>
	 * {@link IDomainObjectListener}s of the {@link IDomainObject} (<i>not</i>
	 * the session) will be notified.
	 *  
	 * @param domainObject
	 */
	<T> void save(IDomainObject<T> domainObject);

	
	/**
	 * Save changes to this (already persistent) object to the
	 * configured object store.
	 * 
	 * <p>
	 * Should delegate to the {@link IDomainObject} for the pojo to do the 
	 * persist; {@link IDomainObjectListener}s of the {@link IDomainObject} 
	 * (<i>not</i> the session) will be notified.
	 *  
	 * @param domainObject
	 */
	void save(Object pojo);


	/**
	 * Returns all attached objects of the supplied class.
	 * 
	 * @param domainClass
	 * @return
	 */
	<T> List<IDomainObject<T>> footprintFor(IDomainClass<T> domainClass);

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

	/**
	 * Returns the {@link IDomainObject} that is wrapping the supplied pojo.
	 * 
	 * <p>
	 * The class of the pojo is required so that the returned
	 * {@link IDomainObject} is correct parameterized. 
	 * 
	 * <p>
	 * Use {@link #hasDomainObjectFor(Object)} to check before hand (to avoid
	 * an exception being thrown).
	 * 
	 * @param <T>
	 * @param pojo
	 * @param pojoClass to parameterize the return. 
	 * @return
	 * @throws IllegalStateException if no such domain object.
	 */
	<T> IDomainObject<T> getDomainObjectFor(Object pojo, Class<T> pojoClass);

	/**
	 * Whether there is a {@link IDomainObject} in this session that
	 * is wrapping the supplied pojo.
	 * 
	 * @param pojo
	 * @return true if there is a wrapper. 
	 */
	public boolean hasDomainObjectFor(Object pojo);

}
