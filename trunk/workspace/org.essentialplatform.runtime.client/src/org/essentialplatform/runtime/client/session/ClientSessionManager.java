package org.essentialplatform.runtime.client.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.client.session.event.ISessionManagerListener;
import org.essentialplatform.runtime.client.session.event.SessionManagerEvent;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.session.ObjectStoreHandleList;

public final class ClientSessionManager implements IClientSessionManager {

	private static ClientSessionManager __instance = new ClientSessionManager();
	/**
	 * Singleton access.
	 * 
	 * @return
	 */
	public static ClientSessionManager instance() {
		return __instance;
	}

	/**
	 * Private visibility so can only be instantiated as a singleton.
	 */
	private ClientSessionManager() {
		// do nothing
	}

	///////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////
	
	private Map<IDomain, ObjectStoreHandleList<IClientSession>> _sessionListByDomain = 
		new HashMap<IDomain, ObjectStoreHandleList<IClientSession>>();

	/*
	 * @see org.essentialplatform.runtime.session.ISessionManager#defineSession(org.essentialplatform.core.domain.IDomain, java.lang.String)
	 */
	public IClientSession defineSession(SessionBinding sessionBinding) {
		Domain domain = Domain.instance(sessionBinding.getDomainName());
		ClientSession session = new ClientSession(sessionBinding);
		ObjectStoreHandleList<IClientSession> sessionList = _sessionListByDomain.get(domain);
		if (sessionList == null) {
			sessionList = new ObjectStoreHandleList();
			_sessionListByDomain.put(domain, sessionList);
		}
		sessionList.add(session);
		SessionManagerEvent event = new SessionManagerEvent(this, session);
		for(ISessionManagerListener listener: _listeners) {
			listener.sessionCreated(event);
		}
		switchSessionTo(domain, session.getObjectStoreId());						
		return session;
	}

	
	/*
	 * @see org.essentialplatform.runtime.session.ISessionManager#getCurrentSession(org.essentialplatform.core.domain.IDomain)
	 */
	public IClientSession getCurrentSession(final IDomain domain) {
		ObjectStoreHandleList<IClientSession> sessionList = _sessionListByDomain.get(domain);
		if (sessionList == null) {
			return null;
		}
		return sessionList.getCurrent();
	}

	/*
	 * @see org.essentialplatform.runtime.shared.session.ISessionManager#switchSessionTo(org.essentialplatform.core.domain.Domain, java.lang.String)
	 */
	public IClientSession switchSessionTo(Domain domain, String objectStoreId) {
		ObjectStoreHandleList<IClientSession> sessionList = _sessionListByDomain.get(domain);
		IClientSession session = sessionList.setCurrent(objectStoreId);
		SessionManagerEvent event = new SessionManagerEvent(this, session);
		for(ISessionManagerListener listener: _listeners) {
			listener.sessionNowCurrent(event);
		}
		return session;
	}


	///////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////
	
	public Collection<IClientSession> getAllSessions() {
		Collection<IClientSession> sessions = new ArrayList<IClientSession>();
		for(ObjectStoreHandleList<IClientSession> sessionList: _sessionListByDomain.values()) {
			sessions.addAll(sessionList.getAll());
		}
		return sessions;
	}


	/**
	 * Primarily for testing purposes; resets all sessions (using
	 * {@link IClientSession#reset()}) and then clears hash of sessions held by the
	 * manager itself.
	 * 
	 */
	public void reset() {
		for(ObjectStoreHandleList<IClientSession> sessionList: _sessionListByDomain.values()) {
			sessionList.reset();
		}
		_sessionListByDomain.clear();
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

	public void removeSession(IDomain domain, String objectStoreId) {
		ObjectStoreHandleList<IClientSession> sessionList = _sessionListByDomain.get(domain);
		IClientSession session = sessionList.remove(objectStoreId);
		SessionManagerEvent event = new SessionManagerEvent(this, session);
		for(ISessionManagerListener listener: _listeners) {
			listener.sessionRemoved(event);
		}
	}

	public synchronized void removeSessionManagerListener(ISessionManagerListener pListener) {
		_listeners.remove(pListener);
	}

}
