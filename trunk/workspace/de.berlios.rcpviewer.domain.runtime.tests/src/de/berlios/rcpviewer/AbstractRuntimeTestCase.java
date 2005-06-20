package de.berlios.rcpviewer;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;

/**
 * Sets up a default {@link Domain}, {@link SessionManager}, 
 * {@link SessionFactory} (for this domain and with an 
 * {@link InMemoryObjectStore} and a {@link Session} for this.
 * 
 * @author Dan Haywood
 *
 */
public abstract class AbstractRuntimeTestCase extends AbstractTestCase {

	public AbstractRuntimeTestCase(IDomainBuilder domainAnalyzer) {
		super(new RuntimeDomainSpecifics(), domainAnalyzer);
	}
	
	public AbstractRuntimeTestCase(String name, IDomainBuilder domainAnalyzer) {
		super(name, new RuntimeDomainSpecifics(), domainAnalyzer);
	}
	
	protected Domain domain;
	protected SessionManager sessionManager;
	protected ISession session;
	protected IObjectStore objectStore;
	protected void setUp() throws Exception {
		super.setUp();
		sessionManager = SessionManager.instance();
		domain = Domain.instance(ProgModelConstants.DEFAULT_DOMAIN_NAME);
		objectStore = new InMemoryObjectStore();
		session = sessionManager.createSession(domain, objectStore);
	}

	protected void tearDown() throws Exception {
		domain = null;
		sessionManager = null;
		session.reset();
		session = null;
		objectStore.reset();
		objectStore = null;
		Domain.resetAll();
		SessionManager.instance().reset();
		super.tearDown();
	}



}
