package de.berlios.rcpviewer.session;



/**
 * Listeners will be notified of any changes to a specific 
 * {@link IDomainObject.IObjectReference} of a particular {@link IDomainObject}.
 * 
 * @author Dan Haywood
 */
public interface IDomainObjectReferenceListener {

	/**
	 * An collection reference of the {@link IDomainObject} has been added to.
	 * 
	 */
	public void collectionAddedTo(DomainObjectReferenceEvent event);
	

	/**
	 * An collection reference of the {@link IDomainObject} has been removed 
	 * from.
	 * 
	 */
	public void collectionRemovedFrom(DomainObjectReferenceEvent event);


	/**
	 * A reference has been changed in some way.
	 * 
	 * @param event
	 */
	public void referenceChanged(DomainObjectReferenceEvent event);
	
	/**
	 * The prerequisites of a reference of the {@link IDomainObject} have been 
	 * changed.
	 *
	 * <p>
	 * Extended semantics.
	 */
	public void referencePrerequisitesChanged(ExtendedDomainObjectReferenceEvent event);

}
