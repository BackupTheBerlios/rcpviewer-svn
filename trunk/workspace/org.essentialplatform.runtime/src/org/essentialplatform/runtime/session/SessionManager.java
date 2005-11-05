package org.essentialplatform.runtime.session;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.session.event.ISessionManagerListener;
import org.essentialplatform.runtime.session.event.SessionManagerEvent;

public final class SessionManager implements ISessionManager {

	/**
	 * Private visibility so can only be instantiated as a singleton.
	 *
	 */
	private SessionManager() {
		// do nothing
	}
	
	private static SessionManager __instance = new SessionManager();
	private String _currentSessionId = null;
	private Map<String, ISession> _sessionsById = new HashMap<String, ISession>();
	private ArrayList<ISessionManagerListener> _listeners = new ArrayList<ISessionManagerListener>();
	

	public ISession createSession(IDomain domain, IObjectStore objectStore) {
		Session session = new Session(nextId(), domain, objectStore);
		_sessionsById.put(session.getId(), session);
		SessionManagerEvent event = new SessionManagerEvent(this, session);
		for(ISessionManagerListener listener: _listeners) {
			listener.sessionCreated(event);
		}
		this.switchSessionTo(session.getId());						
		return session;
	}

	/**
	 * The id of the {@link ISession} set to be the current session.
	 */
	public String getCurrentSessionId() {
		return _currentSessionId;
	}

	/**
	 * Change the current session to that indicated by the session Id.
	 */
	public void switchSessionTo(final String currentSessionId) {
		ISession session = _sessionsById.get(currentSessionId);
		if (session == null) {
			throw new IllegalArgumentException("No such session id:"+currentSessionId);
		}
		_currentSessionId = currentSessionId;
		SessionManagerEvent event = new SessionManagerEvent(this, session);
		for(ISessionManagerListener listener: _listeners) {
			listener.sessionNowCurrent(event);
		}

	}

	/**
	 * Singleton access.
	 * 
	 * @return
	 */
	public static SessionManager instance() {
		return __instance;
	}
	
	/**
	 * Returns a GUID guaranteed to be unique across all VMs.
	 * 
	 * <p>
	 * Uses {@link java.rmi.dgc.VMID}.
	 */
	public String nextId() {
		return new VMID().toString();
	}

	public ISession get(String id) {
		return _sessionsById.get(id);
	}


	/**
	 * Primarily for testing purposes; resets all sessions (using
	 * {@link ISession#reset()}) and then clears hash of sessions held by the
	 * manager itself.
	 * 
	 */
	public void reset() {
		for(ISession session: _sessionsById.values()) {
			session.reset();
		}
		this._sessionsById.clear();
	}

	public synchronized void addSessionManagerListener(final ISessionManagerListener listener) {
		if (!_listeners.contains(listener)) {
			_listeners.add(listener);
		}
	}

	public void removeSession(String sessionId) {
		ISession session = _sessionsById.get(sessionId);
		_sessionsById.remove(session);		
		SessionManagerEvent event = new SessionManagerEvent(this, session);
		for(ISessionManagerListener listener: _listeners) {
			listener.sessionRemoved(event);
		}
	}

	public synchronized void removeSessionManagerListener(ISessionManagerListener pListener) {
		_listeners.remove(pListener);
	}

	public Collection<ISession> getAllSessions() {
		return new ArrayList<ISession>(_sessionsById.values());
	}
	

}
