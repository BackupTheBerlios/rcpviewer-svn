package de.berlios.rcpviewer.metamodel;

/**
 * Listeners will be notified of any changes to the metamodel.
 * 
 * <p>
 * This allows an in-flight running application to change its rendering, eg if
 * hot code deployment.
 * 
 * @author Dan Haywood
 */
public interface IDomainListener {

	/**
	 * An entire metamodel has been modified.
	 * 
	 * <p>
	 * Invoked when there are numerous changes to classes.
	 */
	public void metaModelModified(DomainEvent event);

	/**
	 * A {@link IDomainClass}es has been modified (that is, its attributes,
	 * operations or references have changed).
	 * iots
	 * 
	 * <p>
	 * Invoked when the changes to the metamodel are localized.  If several
	 * classes have changed, then an event will be created for each.
	 * 
	 * <p>
	 * TODO: is this the best design?  would a listener need to know about the
	 * complete set of changes so that it can do the right thing?
	 * 
	 * @param classes
	 */
	public void domainClassModified(DomainClassEvent event);
	
	
}
