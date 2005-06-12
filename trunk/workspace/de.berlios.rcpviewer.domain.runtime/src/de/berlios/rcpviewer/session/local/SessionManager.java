package de.berlios.rcpviewer.session.local;

import java.rmi.dgc.VMID;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionManager;
import de.berlios.rcpviewer.session.ISessionManagerListener;

public final class SessionManager implements ISessionManager {

	private static SessionManager instance = new SessionManager();
	private static String _currentSessionId= null;
	private Map<String, ISession> sessionById = new HashMap<String, ISession>();
	private ArrayList<ISessionManagerListener> _listeners= new ArrayList<ISessionManagerListener>();
	
	// REVIEW_CHANGE added method - ted
	public String addSession(Session pSession) {
		String id= nextId();
		sessionById.put(id, pSession);
		pSession.setId(id);
		return id;
	}

	// REVIEW_CHANGE added method - ted
	public String getCurrentSession() {
		return _currentSessionId;
	}

	// REVIEW_CHANGE added method - ted
	public void setCurrentSession(String pId) {
		if (sessionById.containsKey(pId) == false)
			throw new InvalidParameterException("No such session id:"+pId);
		_currentSessionId= pId;
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

	public synchronized void addSessionManagerListener(ISessionManagerListener pListener) {
		if (!_listeners.contains(pListener))
			_listeners.add(pListener);
	}

	public void removeSession(String sessionId) {
		Object object= sessionById.get(sessionId);
		if (object != null)
			sessionById.remove(object);		
	}

	public synchronized void removeSessionManagerListener(ISessionManagerListener pListener) {
		_listeners.remove(pListener);
	}

	public Collection<ISession> getAllSessions() {
		return new ArrayList<ISession>(sessionById.values());
	}
	

}
