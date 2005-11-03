package org.essentialplatform.runtime.session;

import java.util.List;
import java.util.Set;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.persistence.IObjectStore;

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
	 * The {@link Domain} (or schema, or metamodel) that holds the
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
	 * Creates a new pojo wrapped in an {@link IDomainObject}, attaches to the 
	 * session, and persists if appropriate.
	 * 
	 * <p>
	 * The object will be persisted only if it supports it (is not annotated as 
	 * {@link TransientOnly}, or equiv.)  If it does, it will be
	 * set to {@link IPersistable.PersistState#PERSISTENT} and an 
	 * {@link InstantiationChange} added to the appropriate 
	 * {@link ITransaction}.
	 * 
	 * @param domainClass
	 * @return an {@link IDomainObject} wrapping a newly created pojo.
	 */
	<T> IDomainObject<T> create(IDomainClass domainClass);

	
	/**
	 * Rereates a pojo that has previously been persisted, wraps in a
	 * {@link IDomainObject}, and attaches to the session.
	 * 
	 * <p>
	 * The {@link IDomainObject} will have a persistence state of
	 * {@link IPersistable.PersistState#PERSISTED} and a resolve state of
	 * {@link IResolvable.ResolveState#UNRESOLVED}. 
	 * 
	 * @param domainClass
	 * @return a persisted (though still-to-be-resolved) {@link IDomainObject},
	 *         attached to the supplied session.
	 */
	<T> IDomainObject<T> recreate(IDomainClass domainClass);


	/**
	 * Deletes the pojo wrapped by the supplied {@link IDomainObject}.
	 * 
	 * <p>
	 * The pojo will be enrolled into a {@link ITransaction}, and won't
	 * actually be deleted from the persistent object store until the 
	 * transaction commits.
	 * 
	 * @param <T>
	 * @param pojo
	 */
	<T> void delete(IDomainObject<T> pojo);
	

	/**
	 * Deletes the pojo.
	 * 
	 * <p>
	 * The pojo will be enrolled into a {@link ITransaction}, and won't
	 * actually be deleted from the persistent object store until the 
	 * transaction commits.
	 * 
	 * @param <T>
	 * @param pojo
	 */
	public void delete(Object pojo);

	
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
	 * Note that <i>no</i> {@link ITransactionListener}s will be notified. 
	 * 
	 * <p>
	 * Primarily of use for testing.
	 */
	void reset();

	
	/**
	 * Returns all attached objects of the supplied class.
	 * 
	 * @param domainClass
	 * @return
	 */
	List<IDomainObject<?>> footprintFor(IDomainClass domainClass);

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

	/**
	 * Returns all {@link IObservedFeature}s that are currently referenced.
	 * 
	 * <p>
	 * The session itself holds only weak references to these features so that
	 * once all observers of a feature are GC'ed, then the session will not
	 * in itself keep a reference to the object.  Conversely, the caller of 
	 * this method should not hold a strong reference to the returned
	 * objects (unless it is acting as an actual observer). 
	 * 
	 * @return
	 */
	public Set<IObservedFeature> getObservedFeatures();

	/**
	 * Make the session aware of a new {@link IObservedFeature} such that it
	 * can return it in {@link #getObservedFeatures()}.
	 * 
	 * @param observedFeature
	 */
	public void addObservedFeature(IObservedFeature observedFeature);
}
