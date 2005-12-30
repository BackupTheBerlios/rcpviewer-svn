package org.essentialplatform.runtime.shared.session;

import java.util.HashMap;
import java.util.Map;

import org.essentialplatform.runtime.client.session.IClientSession;

/**
 * Maintains a set of sessions, and the current session, all for a given domain.
 * 
 * cf ObjectStoreList on the client-side.
 * 
 * @author Dan Haywood
 */
public final class SessionList {
	
	private Map<String,IClientSession> _sessionById = new HashMap<String,IClientSession>();
	
	/**
	 * Adds a session and makes it the default.
	 * @param session
	 */
	public void add(IClientSession session) {
		_sessionById.put(session.getId(), session);
		_current = session;
	}
	
	/**
	 * Removes the session; if it was the current session then that is set to 
	 * <tt>null</tt>.
	 * 
	 * @param session
	 * @return <tt> iff the session was contained in the list.
	 */
	public boolean remove(IClientSession session) {
		if (_current == session) {
			_current = null;
		}
		return _sessionById.remove(session.getId()) != null;
	}
	
	private IClientSession _current = null;
	public IClientSession getCurrent() {
		return _current;
	}

	public IClientSession setCurrent(String id) {
		IClientSession session = _sessionById.get(id); 
		if (session == null) {
			throw new IllegalStateException("Could not locate session '" + session + "'");
		}
		_current = session;
		return session;
	}

}
