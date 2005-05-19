package de.berlios.rcpviewer.session;

import java.util.Collection;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.IDomainClass;

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
	
	public IDomainClass<T> getDomainClass();
	
	public T getPojo();
	
	/**
	 * Whether this object has been persisted.
	 * @return
	 */
	public boolean isPersistent();
	
	/**
	 * Persist this object.
	 * 
	 * <p>
	 * Any {@link IDomainObjectListener}s of the object will be notified.
	 *  
	 * @throws IllegalStateException if already persisted.
	 */
	public void persist();
	
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
	 */
	public void invokeOperation(EOperation operation, Object[] args);

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

}
