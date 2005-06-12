package de.berlios.rcpviewer.session;

/**
 * Listeners will be notified of any changes to an {@link ISessionManager}.
 * 
 * @author ted stockwell
 */
public interface ISessionManagerListener {

	/**
	 * A {@link de.berlios.rcpviewer.session.ISession} has been 
	 * added to the manager.
	 * 
	 */
	public void sessionAdded(SessionManagerEvent event);

	/**
	 * An {@link de.berlios.rcpviewer.session.ISession} has been 
	 * removed from the manager.
	 * 
	 */
	public void sessionRemoved(SessionManagerEvent event);

	
	
}
