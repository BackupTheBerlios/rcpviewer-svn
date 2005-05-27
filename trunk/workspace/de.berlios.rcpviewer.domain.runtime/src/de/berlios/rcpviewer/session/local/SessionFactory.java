package de.berlios.rcpviewer.session.local;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionFactory;
import de.berlios.rcpviewer.session.ISessionManager;

public class SessionFactory implements ISessionFactory {


	public ISession createSession() {
		return createSession(getObjectStore());
	}

	public ISession createSession(IObjectStore objectStore) {
		assertInitialized();
		return new Session(getSessionManager().nextId(), getDomain(), objectStore);	
	}

	private void assertInitialized() {
		boolean domainNameInitialized = domainName != null;
		boolean objectStoreInitialized = objectStore != null;
		boolean sessionManagerInitialized = sessionManager != null;
		if (!domainNameInitialized || 
			!objectStoreInitialized || 
			!sessionManagerInitialized) {
			throw new IllegalStateException(
				"Not initialized (missing " +
				(!domainNameInitialized?"domain, ":"") +
				(!objectStoreInitialized?"objectStore, ":"") +
				(!sessionManagerInitialized?"sessionManager, ":"") + ")");
		}
	}

	public IDomain getDomain() {
		assertInitialized();
		return Domain.instance(getDomainName());
	}
	
	// Dependency Injection: start
	private IObjectStore objectStore;
	public IObjectStore getObjectStore() {
		return objectStore;
	}
	public void setObjectStore(IObjectStore objectStore) {
		if (this.objectStore != null) {
			throw new IllegalStateException("ObjectStore already configured.");
		}
		if (objectStore == null) {
			throw new IllegalArgumentException("Object store is mandatory.");
		}
		this.objectStore = objectStore;
	}
	
	private String domainName;
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		if (this.domainName != null) {
			throw new IllegalStateException("Domain already configured.");
		}
		if (domainName == null) {
			throw new IllegalArgumentException("Domain name is mandatory.");
		}
		this.domainName = domainName;
	}

	private ISessionManager sessionManager;
	public ISessionManager getSessionManager() {
		return sessionManager;
	}
	public void setSessionManager(final ISessionManager sessionManager) {
		if (this.sessionManager != null) {
			throw new IllegalStateException("SessionManager already configured.");
		}
		if (sessionManager == null) {
			throw new IllegalArgumentException("Session manager is mandatory.");
		}
		this.sessionManager = sessionManager;
	}
	// Dependency Injection: end

}
