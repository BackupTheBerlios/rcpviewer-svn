package de.berlios.rcpviewer.session;



/**
 * Listeners will be notified of any changes to a specific 
 * {@link IDomainObject.IObjectReference} of a particular {@link IDomainObject}.
 * 
 * @author Dan Haywood
 */
public interface IExtendedDomainObjectReferenceListener {


	/**
	 * The prerequisites of a reference of the {@link IDomainObject} have been 
	 * changed.
	 * 
	 */
	public void referencePrerequisitesChanged(ExtendedDomainObjectReferenceEvent event);

}
