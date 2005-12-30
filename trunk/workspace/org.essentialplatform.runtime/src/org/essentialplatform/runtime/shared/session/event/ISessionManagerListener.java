package org.essentialplatform.runtime.shared.session.event;

import org.essentialplatform.runtime.shared.session.IClientSession;
import org.essentialplatform.runtime.shared.session.IClientSessionManager;

/**
 * Listeners will be notified of any changes to an {@link IClientSessionManager}.
 * 
 * @author ted stockwell
 */
public interface ISessionManagerListener {

	/**
	 * A {@link org.essentialplatform.runtime.shared.session.IClientSession} has been 
	 * created (and added) to the manager.
	 * 
	 * <p>
	 * Since when a session is created it is then made current, this event is
	 * immediately followed by a call to
	 * {@link #sessionNowCurrent(SessionManagerEvent)}.  As such, implementors
	 * of this interface may be interested in the latter method rather than 
	 * this one.   
	 * 
	 */
	public void sessionCreated(SessionManagerEvent event);

	/**
	 * The current {@link org.essentialplatform.runtime.shared.session.IClientSession} has been 
	 * changed.
	 * 
	 */
	public void sessionNowCurrent(SessionManagerEvent event);

	/**
	 * An {@link org.essentialplatform.runtime.shared.session.IClientSession} has been 
	 * removed from the manager.
	 * 
	 */
	public void sessionRemoved(SessionManagerEvent event);

	
	
}
