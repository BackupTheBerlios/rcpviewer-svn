package de.berlios.rcpviewer.session;



/**
 * Listeners will be notified of any changes to a specific 
 * {@link IDomainObject}.
 * 
 * <p>
 * The archetypal user of this interface is the component of rcpviewer that 
 * performs the rendering (the "UI").  Under the Naked Objects pattern an 
 * {@link IDomainObject} instance would be rendered 1:1 by the UI, eg as an
 * editor using the Eclipse Forms API.  Thus we have an instance of MVC: the
 * {@link IDomainObject} is the model, the editor is the view, and the
 * Eclipse platform itself provides (at least some of the responsibilities of)
 * the controller, eg tracking keystrokes and mouse movements. 
 * 
 * @author Dan Haywood
 */
public interface IDomainObjectListener {

	/**
	 * An attribute of the {@link IDomainObject} has been changed.
	 * 
	 */
	public void attributeChanged(DomainObjectAttributeEvent event);

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
	 * The {@link IDomainObject} has been persisted.
	 * 
	 * @param event
	 */
	public void persisted(DomainObjectEvent event);
}
