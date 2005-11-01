package org.essentialplatform.session;



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

	/**
	 * The prerequisites of an attribute of the {@link IDomainObject} have been 
	 * changed.
	 * 
	 * <p>
	 * Extended semantics.
	 */
	public void attributePrerequisitesChanged(ExtendedDomainObjectAttributeEvent event);

}
