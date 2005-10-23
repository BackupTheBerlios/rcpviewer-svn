package de.berlios.rcpviewer.session;



/**
 * Listeners will be notified of any changes to a specific 
 * {@link IDomainObject.IObjectAttribute} of a specific {@link IDomainObject}.
 * 
 * @author Dan Haywood
 */
public interface IDomainObjectAttributeListener {

	/**
	 * An attribute of the {@link IDomainObject} has been changed.
	 * 
	 */
	public void attributeChanged(DomainObjectAttributeEvent event);

}
