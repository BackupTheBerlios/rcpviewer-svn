package org.essentialplatform.runtime.shared.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.shared.persistence.IObjectStore;
import org.essentialplatform.runtime.shared.session.event.ISessionManagerListener;
import org.essentialplatform.runtime.shared.session.event.SessionManagerEvent;

public final class SessionManager implements ISessionManager {

	private static SessionManager __instance = new SessionManager();
	/**
	 * Singleton access.
	 * 
	 * @return
	 */
	public static SessionManager instance() {
		return __instance;
	}

	///////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////
	

	/**
	 * Private visibility so can only be instantiated as a singleton.
	 */
	private SessionManager() {
		// do nothing
	}

	///////////////////////////////////////////////////////////////////
	// Hashes
	// TODO: should we also hold (SessionBinding,ISession)?  This will
	// be needed on the ServerSessionManager, but perhaps not on
	// the client?
	///////////////////////////////////////////////////////////////////
	
	private Map<String, ISession> _sessionsById = new HashMap<String, ISession>();
	
	/*
	 * @see org.essentialplatform.runtime.session.ISessionManager#defineSession(org.essentialplatform.core.domain.IDomain, java.lang.String)
	 */
	public ISession defineSession(SessionBinding sessionBinding) {
		Domain domain = Domain.instance(sessionBinding.getDomainName());
		Session session = new Session(sessionBinding);
		_sessionsById.put(session.getId(), session);
		SessionList sessionList = _sessionListByDomain.get(domain);
		if (sessionList == null) {
			sessionList = new SessionList();
			_sessionListByDomain.put(domain, sessionList);
		}
		sessionList.add(session);
		SessionManagerEvent event = new SessionManagerEvent(this, session);
		for(ISessionManagerListener listener: _listeners) {
			listener.sessionCreated(event);
		}
		switchSessionTo(session.getId());						
		return session;
	}

	
	private Map<IDomain, SessionList> _sessionListByDomain = new HashMap<IDomain, SessionList>();
	/*
	 * @see org.essentialplatform.runtime.session.ISessionManager#getCurrentSession(org.essentialplatform.core.domain.IDomain)
	 */
	public ISession getCurrentSession(final IDomain domain) {
		SessionList sessionList = _sessionListByDomain.get(domain);
		if (sessionList == null) {
			return null;
		}
		return sessionList.getCurrent();
	}

	/*
	 * @see org.essentialplatform.runtime.shared.session.ISessionManager#switchSessionTo(org.essentialplatform.core.domain.Domain, java.lang.String)
	 */
	public ISession switchSessionTo(Domain domain, String objectStoreId) {
		SessionList sessionList = _sessionListByDomain.get(domain);
		return sessionList.setCurrent(objectStoreId);
	}


	/**
	 * Change the current session (for the inferred domain) to that indicated 
	 * by the session Id.
	 */
	public void switchSessionTo(final String currentSessionId) {
		
		ISession session = _sessionsById.get(currentSessionId);
		if (session == null) {
			throw new IllegalArgumentException("No such session id:"+currentSessionId);
		}
		IDomain domain = session.getDomain();
		SessionList sessionList = _sessionListByDomain.get(domain);
		if (sessionList == null) {
			throw new IllegalStateException("Could not locate session list for domain '" + domain + "'");
		}
		sessionList.setCurrent(session.getId());
		SessionManagerEvent event = new SessionManagerEvent(this, session);
		for(ISessionManagerListener listener: _listeners) {
			listener.sessionNowCurrent(event);
		}
	}

	
	///////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////
	
	public Collection<ISession> getAllSessions() {
		return new ArrayList<ISession>(_sessionsById.values());
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

	///////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////
	
	private ArrayList<ISessionManagerListener> _listeners = new ArrayList<ISessionManagerListener>();
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



}
