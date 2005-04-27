package de.berlios.rcpviewer;

import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.IObjectStoreAware;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionAware;
import de.berlios.rcpviewer.session.local.Session;
import junit.framework.TestCase;

/**
 * Aware of certain components.
 * 
 * @author Dan Haywood
 *
 */
public abstract class AbstractTestCase extends TestCase 
	implements /* ISessionAware, */ IObjectStoreAware{

	public AbstractTestCase() { }

	public AbstractTestCase(String name) {
		super(name);
	}

	// DEPENDENCY INJECTION START
	
	private ISession session;
	public ISession getSession() {
		return Session.instance();
	}
	public void setSession(ISession session) {
		this.session = session;
	}


	private IObjectStore objectStore;
	public IObjectStore getObjectStore() {
		return objectStore;
	}
	public void setObjectStore(IObjectStore objectStore) {
		this.objectStore = objectStore;
	}

	// DEPENDENCY INJECTION END

}
