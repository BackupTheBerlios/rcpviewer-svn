package org.essentialplatform.runtime.client.session.event;

import java.util.EventObject;

import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.client.session.IClientSessionManager;

/**
 * Event object for session manager events.
 * 
 * @author ted stockwell
 */
public class SessionManagerEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private IClientSession _session;

	/**
	 * @param source
	 */
	public SessionManagerEvent(final IClientSessionManager source, IClientSession session) {
		super(source);
		_session= session;
	}
	
	/**
	 * Type-safe access to the source of this event.
	 * 
	 * @return the session manager that raised the event.
	 */
	public IClientSessionManager getSessionManager() {
		return (IClientSessionManager)this.getSource();
	}
	
	public IClientSession getSession() {
		return _session;
	}

}
