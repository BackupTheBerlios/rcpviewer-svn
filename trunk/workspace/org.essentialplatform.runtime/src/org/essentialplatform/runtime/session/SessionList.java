package org.essentialplatform.runtime.session;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Maintains a set of sessions, and the current session, all for a given domain.
 * 
 * cf ObjectStoreList on the client-side.
 * 
 * @author Dan Haywood
 */
final class SessionList {
	
	private Set<ISession> sessions = new LinkedHashSet<ISession>();
	
	/**
	 * Adds a session and makes it the default.
	 * @param session
	 */
	void add(ISession session) {
		sessions.add(session);
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
		return sessions.remove(session);
	}
	
	private ISession _current = null;
	ISession getCurrent() {
		return _current;
	}
	void setCurrent(ISession session) {
		if (!sessions.contains(session)) {
			throw new IllegalStateException("Could not locate session '" + session + "'");
		}
		_current = session;
	}


}
