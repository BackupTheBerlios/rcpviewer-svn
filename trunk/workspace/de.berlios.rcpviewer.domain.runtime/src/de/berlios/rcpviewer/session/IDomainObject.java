package de.berlios.rcpviewer.session;

import java.util.Collection;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.extended.ExtendedDomainObject;
import de.berlios.rcpviewer.progmodel.extended.IConstraintSet;

/**
 * A wrapper around a pojo, allowing reflective and generic access to that
 * pojo in the context of a describing meta-model, usually attached to an
 * owning session.
 * 
 * <p>
 * The UI layer interacts with the rest of the framework through the
 * IDomainObject: conceptually a single IDomainObject backs every editor 
 * shown in the UI.  Although this interface exposes the underlying pojo, the 
 * UI layer will not interact with the pojo directly because it necessarily 
 * must be generic (not coupled to any particular class of pojo).  Instead, 
 * this interface exposes methods such as {@link #get(EAttribute)} and
 * {@link #set(EAttribute, Object)} to allow reflective interactions.
 * 
 * <p>
 * Moreover, the IDomainObject also plays the role of <i>model</i> within the 
 * MVC pattern.  Again, the UI layer is the archetypal <i>view</i>, being
 * notified of changes through the {@link IDomainObjectListener} interface. 
 * 
 * <p>
 * TODO: should validate the EOperations and EAttributes, just as we do
 * for IDomainClass.
 * 
 * @author Dan Haywood
 */
public interface IDomainObject<T> {

	public IRuntimeDomainClass<T> getDomainClass();
	
	public T getPojo();
	
	/**
	 * Whether this object has been persisted.
	 * @return
	 */
	public boolean isPersistent();
	
	/**
	 * Persist this object (for the first time).
	 * 
	 * <p>
	 * Any {@link IDomainObjectListener}s of the object will be notified.
	 *  
	 * @throws IllegalStateException if already persisted.
	 */
	public void persist();
	
	/**
	 * Save this already persisted object.
	 * 
	 * @throws IllegalStateException if not yet persisted.
	 */
	public void save();
	
	/**
	 * Distinguishable representation of the domain object in the UI.
	 * 
	 * <p>
	 * TODO: should this be required to be unique.  If not, how
	 * 
	 * @return
	 */
	public String title();

	/**
	 * Convenience method that should return the same as the 
	 * corresponding method in {@link IDomainClass}.
	 * 
	 * @param attributeName
	 * @return
	 */
	public EAttribute getEAttributeNamed(String attributeName);

	/**
	 * Get the specified attribute's current value.
	 * 
	 * @param nameAttribute
	 */
	public Object get(EAttribute nameAttribute);

	/**
	 * Set the specified attribute to the new value.
	 * 
	 * @param nameAttribute
	 * @param newValue
	 */
	public void set(EAttribute nameAttribute, Object newValue);

	/**
	 * Convenience method that should return the same as the 
	 * corresponding method in {@link IDomainClass}.
	 * 
	 * @param operationName
	 * @return
	 */
	public EOperation getEOperationNamed(String operationName);

	/**
	 * Invoke the operation, applying any preconditions before hand.
	 * 
	 * @param operation
	 * @return the return value from the operation (if not void).
	 */
	public Object invokeOperation(EOperation operation, Object[] args);

	/**
	 * Convenience method that should return the same as the 
	 * corresponding method in {@link IDomainClass}.
	 * 
	 * @param operationName
	 * @return
	 */
	public EReference getEReferenceNamed(String referenceName);

	/**
	 * Returns an immutable collection,
	 * 
	 * @param reference
	 * @return
	 */
	public <V> Collection<IDomainObject<V>> getCollection(EReference reference);

	/**
	 * Adds the domain object to the named collection.
	 * 
	 * <p>
	 * Any {@link IDomainObjectListener}s will be notified.
	 * 
	 * @param collection
	 * @param domainObject
	 */
	public <Q> void addToCollection(EReference collection, IDomainObject<Q> domainObject);

	/**
	 * Removes the domain object from the named collection.
	 * 
	 * <p>
	 * Any {@link IDomainObjectListener}s will be notified.
	 * 
	 * @param collection
	 * @param domainObject
	 */
	public <Q> void removeFromCollection(EReference collection, IDomainObject<Q> domainObject);

	/**
	 * Adds domain object listener.
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
	public <T extends IDomainObjectListener> T addDomainObjectListener(T listener);

	
	/**
	 * Removes domain object listener.
	 * 
	 * <p>
	 * If the listener is unknown, does nothing.
	 * 
	 * @param listener
	 */
	public void removeDomainObjectListener(IDomainObjectListener listener);

	
	/**
	 * The id of the {@link ISession} that initially managed this session
	 * (if any).
	 * 
	 * <p>
	 * Attempting to attach a domain object to a session with a different Id
	 * will fail.
	 * 
	 * @return
	 */
	public String getSessionId();

	/**
	 * Clears the session identifier.
	 *
	 * <p>
	 * Normally the session identifier of a domain object is never changed, 
	 * representing the id of the {@link ISession} that originally managed
	 * the domain object.  Even if a domain object is detached from that 
	 * session, the session identifier is retained so that - under normal
	 * circumstances - the domain object may only be re-attached to the same
	 * session.
	 * 
	 * <p>
	 * However, if an object has been detached from a session then it is 
	 * possible using this method to clear this session id, thereby allowing
	 * the domain object to be attached to some other {@link ISession}, 
	 * providing that this new session references to the same {@link RuntimeDomain}.
	 * This capability may be useful for "what-if" analysis and the like.
	 * 
	 * @throws IllegalStateException - if currently attached.
	 */
	public void clearSessionId();

	/**
	 * Whether this domain object is currently attached to a {@link ISession}.
	 * 
	 * <p>
	 * If so, then {#getSession()} will return a non-null result.
	 *  
	 * @return
	 */
	public boolean isAttached();

	/**
	 * Returns an adapter for this object with respect to the adapter of some
	 * programming model.
	 * 
	 * <p>
	 * The XxxDomainObject.class of a programming model is used to identify the
	 * adapter.
	 * 
	 * <p>
	 * For example, to obtain an ExtendedDomainObject for someDomainObject, use:
	 * <code>
	 * IDomainObject<T> dobj = ...;
	 * ExtendedDomainObject<T> edc = dobj.getAdapter(ExtendedDomainObject.class); 
	 * </code>
	 *   
	 * @param <V>
	 * @param pojoClass
	 * @return
	 */
	public <V> V getAdapter(Class<V> domainObjectClass);

}
