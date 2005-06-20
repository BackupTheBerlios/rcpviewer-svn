package de.berlios.rcpviewer.session.local;

import java.rmi.dgc.VMID;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionManager;
import de.berlios.rcpviewer.session.ISessionManagerListener;
import de.berlios.rcpviewer.session.SessionManagerEvent;

public final class SessionManager implements ISessionManager {

	private static SessionManager instance = new SessionManager();
	private static String currentSessionId= null;
	private Map<String, ISession> sessionById = new HashMap<String, ISession>();
	private ArrayList<ISessionManagerListener> listeners= new ArrayList<ISessionManagerListener>();
	

	public ISession createSession(IDomain domain, IObjectStore objectStore) {
		Session session = new Session(nextId(), domain, objectStore);
		sessionById.put(session.getId(), session);
		SessionManagerEvent event = new SessionManagerEvent(this, session);
		for(ISessionManagerListener listener: listeners) {
			listener.sessionCreated(event);
		}
		this.switchSessionTo(session.getId());						
		return session;
	}

	/**
	 * The id of the {@link ISession} set to be the current session.
	 */
	public String getCurrentSessionId() {
		return currentSessionId;
	}

	/**
	 * Change the current session to that indicated by the session Id.
	 */
	public void switchSessionTo(final String currentSessionId) {
		ISession session = sessionById.get(currentSessionId);
		if (session == null) {
			throw new IllegalArgumentException("No such session id:"+currentSessionId);
		}
		this.currentSessionId = currentSessionId;
		SessionManagerEvent event = new SessionManagerEvent(this, session);
		for(ISessionManagerListener listener: listeners) {
			listener.sessionNowCurrent(event);
		}

	}

	/**
	 * Singleton access.
	 * 
	 * @return
	 */
	public static SessionManager instance() {
		return instance;
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
		return sessionById.get(id);
	}


	/**
	 * Primarily for testing purposes; resets all sessions (using
	 * {@link ISession#reset()}) and then clears hash of sessions held by the
	 * manager itself.
	 * 
	 */
	public void reset() {
		for(ISession session: sessionById.values()) {
			session.reset();
		}
		this.sessionById.clear();
	}

	public synchronized void addSessionManagerListener(final ISessionManagerListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeSession(String sessionId) {
		ISession session = sessionById.get(sessionId);
		sessionById.remove(session);		
		SessionManagerEvent event = new SessionManagerEvent(this, session);
		for(ISessionManagerListener listener: listeners) {
			listener.sessionRemoved(event);
		}
	}

	public synchronized void removeSessionManagerListener(ISessionManagerListener pListener) {
		listeners.remove(pListener);
	}

	public Collection<ISession> getAllSessions() {
		return new ArrayList<ISession>(sessionById.values());
	}
	

}
