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
public interface IDomainObject {
	public IDomainClass getDomainClass();
	public Object getPojo();
}
