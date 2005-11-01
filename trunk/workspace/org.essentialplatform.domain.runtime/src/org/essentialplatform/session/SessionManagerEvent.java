package org.essentialplatform.session;

import java.util.EventObject;

/**
 * Event object for session manager events.
 * 
 * @author ted stockwell
 */
public class SessionManagerEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private ISession _session;

	/**
	 * @param source
	 */
	public SessionManagerEvent(final ISessionManager source, ISession session) {
		super(source);
		_session= session;
	}
	
	/**
	 * Type-safe access to the source of this event.
	 * 
	 * @return the session manager that raised the event.
	 */
	public ISessionManager getSessionManager() {
		return (ISessionManager)this.getSource();
	}
	
	public ISession getSession() {
		return _session;
	}

}
