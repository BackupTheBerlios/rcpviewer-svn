package de.berlios.rcpviewer.session;

/**
 * Listeners will be notified of any changes to an {@link ISession}.
 * 
 * @author Dan Haywood
 */
public interface ISessionListener {

	/**
	 * An {@link de.berlios.rcpviewer.metamodel.IDomainObject} has been 
	 * attached to the session.
	 * 
	 */
	public void domainObjectAttached(SessionObjectEvent event);

	/**
	 * An {@link de.berlios.rcpviewer.metamodel.IDomainObject} has been 
	 * dettached to the session.
	 * 
	 */
	public void domainObjectDetached(SessionObjectEvent event);

	
	
}
