package de.berlios.rcpviewer.metamodel;

/**
 * A wrapper around a pojo domain object.
 * 
 * <p>
 * The UI layer interacts with the rest of the framework through the
 * IDomainObject: conceptually a single IDomainObject backs every editor 
 * shown in the UI.
 * 
 * @author Dan Haywood
 */
public interface IDomainObject<T> {
	
	public IDomainClass getDomainClass();
	
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

}
