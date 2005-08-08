package de.berlios.rcpviewer.session;



/**
 * Listeners will be notified of any changes to a specific 
 * {@link IDomainObject.IOperation} of a particular {@link IDomainObject}.
 * 
 * @author Dan Haywood
 */
public interface IExtendedDomainObjectOperationListener {


	/**
	 * The prerequisites of an operation of the {@link IDomainObject} have been 
	 * changed.
	 * 
	 */
	public void operationPrerequisitesChanged(ExtendedDomainObjectOperationEvent event);

}
