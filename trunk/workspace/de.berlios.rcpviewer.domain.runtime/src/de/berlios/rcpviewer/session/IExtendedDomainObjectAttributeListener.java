package de.berlios.rcpviewer.session;



/**
 * Listeners will be notified of any changes to a specific 
 * {@link IExtendedDomainObject.IExtendedAttribute} of a specific {@link IExtendedDomainObject}.
 * 
 * @author Dan Haywood
 */
public interface IExtendedDomainObjectAttributeListener {

	/**
	 * The prerequisites of an attribute of the {@link IDomainObject} have been 
	 * changed.
	 * 
	 */
	public void attributePrerequisitesChanged(ExtendedDomainObjectAttributeEvent event);

}
