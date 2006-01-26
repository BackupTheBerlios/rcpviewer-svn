package org.essentialplatform.runtime.server.session;

import org.essentialplatform.runtime.shared.session.SessionBinding;

public abstract class AbstractServerSessionFactory<V extends IServerSession> implements IServerSessionFactory<V> {

	public AbstractServerSessionFactory(SessionBinding sessionBinding) {
		_sessionBinding = sessionBinding;
	}

	private final SessionBinding _sessionBinding;
	public SessionBinding getSessionBinding() {
		return _sessionBinding;
	}
	
	
	/////////////////////////////////////////
	// TODO: make obsolete... 
	
	/**
	 * TODO: to go, if rename IObjectStoreHandle to ISessionBindingProvider
	 */
	public String getObjectStoreId() {
		return _sessionBinding.getObjectStoreId();
	}
	
	/**
	 * TODO: to get rid of, in favour of session binding.
	 * 
	 * @param objectStoreId
	 */
	public void setObjectStoreId(String objectStoreId) {
		throw new IllegalArgumentException("use setSessionBinding instead");
	}

	/////////////////////////////////////////

}
