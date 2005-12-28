package org.essentialplatform.runtime.shared.session;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains a set of sessions, and the current session, all for a given domain.
 * 
 * cf ObjectStoreList on the client-side.
 * 
 * @author Dan Haywood
 */
final class SessionList {
	
	private Map<String,ISession> _sessionById = new HashMap<String,ISession>();
	
	/**
	 * Adds a session and makes it the default.
	 * @param session
	 */
	void add(ISession session) {
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
	boolean remove(ISession session) {
		if (_current == session) {
			_current = null;
		}
		return _sessionById.remove(session.getId()) != null;
	}
	
	private ISession _current = null;
	ISession getCurrent() {
		return _current;
	}

	ISession setCurrent(String id) {
		ISession session = _sessionById.get(id); 
		if (session == null) {
			throw new IllegalStateException("Could not locate session '" + session + "'");
		}
		_current = session;
		return session;
	}

}
