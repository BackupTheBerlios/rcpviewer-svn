package org.essentialplatform.runtime.shared.session.event;

/**
 * Convenience superclass for implementing {@link ISessionManagerListener}.
 * 
 * @author Dan Haywood
 *
 */
public abstract class AbstractSessionManagerListener implements ISessionManagerListener {

	/**
	 * Do nothing implementation.
	 */
	public void sessionCreated(SessionManagerEvent event) {
		// does nothing
	}

	/**
	 * Do nothing implementation.
	 */
	public void sessionNowCurrent(SessionManagerEvent event) {
		//		 does nothing
	}

	/**
	 * Do nothing implementation.
	 */
	public void sessionRemoved(SessionManagerEvent event) {
		//		 does nothing
	}

}
