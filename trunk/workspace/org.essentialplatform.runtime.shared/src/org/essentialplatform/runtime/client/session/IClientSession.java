package org.essentialplatform.runtime.client.session;

import java.util.List;
import java.util.Set;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.client.session.event.ISessionListener;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IObservedFeature;
import org.essentialplatform.runtime.shared.domain.handle.IHandleAssigner;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;
import org.essentialplatform.runtime.shared.session.IObjectStoreHandle;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.domain.Handle;

/**
 * Binds a {@link IDomain} with the Id of an objectstore.
 * 
 * <p>
 * Put another way, it is the client-side equivalent of an object store 
 * with respect to a particular {@link IDomain}.
 * 
 * <p>
 * Some scenarios:
 * 
 * <h3>Scenario #1: single centralized system</h3>
 * <p>
 * <table>
 * <tr><th>Domain</th><th>ObjectStore</th></tr>
 * <tr><td>HR</td><td>London</td></tr>
 * </table>
 * 
 * <h3>Scenario #2: same domain deployed to multiple locations</h3>
 * <p>
 * <table>
 * <tr><th>Domain</th><th>ObjectStore</th></tr>
 * <tr><td>Shop</td><td>Edinburgh</td></tr>
 * <tr><td>Shop</td><td>London</td></tr>
 * <tr><td>Shop</td><td>Oxford</td></tr>
 * </table>
 * 
 * <h3>Scenario #3: multiple domains</h3>
 * <p>
 * <table>
 * <tr><th>Defects</th><th>ChildBenefit</th></tr>
 * <tr><td>SourceCode</td><td>CB_SVNRepos</td></tr>
 * </table>
 * 
 * <p>
 * Each (domain, objectstore) tuple is a Session.  As such, it represents a 
 * connected space of pojos (each wrapped in a {@link IDomainObject}) known to 
 * the current user.   This is, basically, the same semantics as Hibernate.
 * 
 * <p>
 * For convenience, on the client, each session has a unique identifier.  This
 * identifier is <i>not</i> used in the server-side however: it is the
 * (domainName, objectStoreName) information that is needed.  (Otherwise, if
 * the server did keep track of session Ids, there would be distributed GC
 * issues to contend with).
 * 
 * <p>
 * If there is only ever one session per domain, then there is never any 
 * ambiguity with respect to which object store to put a new domain object 
 * into.  However, if there is >1 session per domain, then a mechanism is 
 * required - namely the "current session" (per domain).  For example:
 * 
 * <table>
 * <tr><th>SessId</th><th>Domain</th><th>ObjectStore</th><th>Current?</th></tr>
 * <tr><td>1</td><td>Shop</td><td>Edinburgh</td><td>N</td></tr>
 * <tr><td>2</td><td>Shop</td><td>London</td><td>N</td></tr>
 * <tr><td>3</td><td>Shop</td><td>Oxford</td><td>Y</td></tr>
 * <tr><td>4</td><td>Defects</td><td>ChildBenefit</td><td>Y</td></tr>
 * <tr><td>5</td><td>Defects</td><td>Pensions</td><td>N</td></tr>
 * </table>
 * <p>
 * Any new domain objects in the <tt>Shop</tt> domain would go into 
 * session #3 = Oxford, and new domain objects in the <tt>Defects</tt> domain 
 * woiuld go into session #4 = ChildBenefit
 * 
 * @author Dan Haywood
 */
public interface IClientSession extends IObjectStoreHandle, IHandleMap {

	
	/**
	 * The {@link Domain} (or schema, or metamodel) that holds the
	 * {@link IDomainClass}es for which every {@link IDomainObject} managed
	 * by this session must correspond.
	 * 
	 * <p>
	 * Implementation does not require that this information is preserved
	 * during serialization.
	 *   
	 * @return
	 */
	IDomain getDomain();
	
	/**
	 * Stores the (domainName, objectStoreId) tuple.
	 * 
	 * <p>
	 * The {@link SessionBinding} (which is serializable) is copied down into 
	 * each managed {@link IDomainObject} so that the session context can be
	 * serialized across the wire as required.
	 * 
	 * @return
	 */
	SessionBinding getSessionBinding();

	
	
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
	 * Returns the {@link IHandleAssigner} used for assigning {@link Handle}s 
	 * 
	 * <p>
	 * Normally the value of such {@link Handle}s is temporary; the object is
	 * persisted server-side and the handle updated with the Id from the
	 * object store.  However, transient-only objects will never have their
	 * handle updated.
	 * 
	 * @return
	 */
	public IHandleAssigner getHandleAssigner();
	/**
	 * Inject the implementation of the {@link IHandleAssigner}.
	 * 
	 * <p>
	 * Implementations should set up a reasonable default implementation such 
	 * that it is not mandatory to call this method as part of initialization.
	 * 
	 * @see #getHandleAssigner()
	 * @param handleAssigner
	 */
	public void setHandleAssigner(IHandleAssigner handleAssigner);
	

	
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
