package de.berlios.rcpviewer.session;



/**
 * Listeners will be notified of any changes to a specific 
 * {@link IDomainObject.IObjectOperation} of a particular {@link IDomainObject}.
 * 
 * @author Dan Haywood
 */
public interface IDomainObjectOperationListener {

	public void operationInvoked(DomainObjectOperationEvent event);
}
