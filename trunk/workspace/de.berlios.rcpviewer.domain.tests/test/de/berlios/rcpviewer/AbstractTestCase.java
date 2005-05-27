package de.berlios.rcpviewer;

import junit.framework.TestCase;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionFactory;
import de.berlios.rcpviewer.session.local.SessionManager;

/**
 * Sets up a default {@link Domain}, {@link SessionManager}, 
 * {@link SessionFactory} (for this domain and with an 
 * {@link InMemoryObjectStore} and a {@link Session} for this.
 * 
 * @author Dan Haywood
 *
 */
public abstract class AbstractTestCase extends TestCase {

	public AbstractTestCase() { }

	public AbstractTestCase(String name) {
		super(name);
	}
	
	protected Domain domain;
	protected SessionManager sessionManager;
	protected SessionFactory sessionFactory;
	protected ISession session;
	protected IObjectStore objectStore;
	protected void setUp() throws Exception {
		super.setUp();
		sessionManager = SessionManager.instance();
		sessionFactory = new SessionFactory();
		sessionFactory.setSessionManager(sessionManager);
		sessionFactory.setDomainName(ProgModelConstants.DEFAULT_DOMAIN_NAME);
		objectStore = new InMemoryObjectStore();
		sessionFactory.setObjectStore(objectStore);
		session = sessionFactory.createSession();
		domain = Domain.instance();
	}

	protected void tearDown() throws Exception {
		domain = null;
		sessionManager = null;
		sessionFactory = null;
		session.reset();
		session = null;
		objectStore.reset();
		objectStore = null;
		Domain.resetAll();
		SessionManager.instance().reset();
		super.tearDown();
	}



}
