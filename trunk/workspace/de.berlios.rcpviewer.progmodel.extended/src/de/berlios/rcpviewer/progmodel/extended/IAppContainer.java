package de.berlios.rcpviewer.progmodel.extended;


/**
 * Facade for the Ubiquity platform from the perspective of a domain object.
 * 
 * <p>
 * Many domain objects will be unaware of the platform.  However, the platform
 * does need to know of objects that are being created, primarily so that any 
 * dependencies on system services can be injected into the newly created
 * domain object.  Thus the creating domain objects must create new objects
 * <i>through</i> the platform.
 * 
 * <p>
 * Moreover the app container can provide some more specialized services to
 * domain objects that require them.
 * 
 * @author Dan Haywood
 *
 */
public interface IAppContainer {

	/**
	 * Create a still-to-be-persisted instance of the specified class, using
	 * its no-arg constructor.
	 * 
	 * <p>
	 * Any system services and repositories will be automatically injected into
	 * the newly created pojo.
	 * 
	 * <p>
	 * Implementation notes: the platform will create an {@link IDomainObject}
	 * to allow this newly created pojo to be rendered.
	 * 
	 * @param <V>
	 * @param javaClass
	 * @return
	 */
	<V> V createTransient(Class<V> javaClass);

	/**
	 * Whether the supplied pojo is persistent or not.
	 * 
	 * <p>
	 * Occasionally some domain objects need to know where they are in their 
	 * lifecycle (still transient or now persisted).  This provides one way
	 * for them to find out easily.
	 * 
	 * @param pojo
	 * @return
	 */
	boolean isPersistent(Object pojo);

	/**
	 * Delete the specified object.
	 * 
	 * <p>
	 * The object will be removed from persistent storage context; the caller
	 * should discard their own reference to the pojo so that it can be GC'ed.
	 * 
	 * @param pojo
	 */
	void delete(Object pojo);

}
