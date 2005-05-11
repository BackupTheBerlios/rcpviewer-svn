package de.berlios.rcpviewer.metamodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

/**
 * A wrapper around a pojo, allowing reflective and generic access to that
 * pojo in the context of a describing meta-model.
 * 
 * <p>
 * The UI layer interacts with the rest of the framework through the
 * IDomainObject: conceptually a single IDomainObject backs every editor 
 * shown in the UI.
 * 
 * <p> 
 * Note that although this interface exposes the underlying pojo, the UI layer
 * will not interact with the pojo directly because it necessarily must be
 * generic (not coupled to any particular class of pojo).  So instead, this
 * interface exposes methods such as {@link #get(EAttribute)} and
 * {@link #set(EAttribute, Object)} to allow reflective interactions.
 * 
 * <p>
 * TODO: At the moment the need for get and set methods probably does not 
 * seem that compelling.  However, in time will extend to include other aspects
 * of the choreography between the UI and the underlying pojo, including
 * fetching default values of operations, and establishing whether an attribute
 * or operation is (a) visible and if so then (b) enabled. 
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

}
