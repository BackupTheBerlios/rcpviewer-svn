package org.essentialplatform;

import org.essentialplatform.domain.Domain;
import org.essentialplatform.domain.Domain;
import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.persistence.IObjectStore;
import org.essentialplatform.persistence.inmemory.InMemoryObjectStore;
import org.essentialplatform.progmodel.standard.ProgModelConstants;
import org.essentialplatform.session.ISession;
import org.essentialplatform.session.local.SessionManager;
import org.essentialplatform.transaction.ITransactionManager;
import org.essentialplatform.transaction.internal.TransactionManager;
import org.essentialplatform.progmodel.standard.EssentialProgModelDomainBuilder;

/**
 * Sets up a default {@link Domain}, {@link SessionManager}, 
 * {@link SessionFactory} (for this domain and with an 
 * {@link InMemoryObjectStore} and a {@link Session} for this.
 * 
 * @author Dan Haywood
 *
 */
public abstract class AbstractRuntimeTestCase extends AbstractTestCase {

	/**
	 * Provides null domainBuilder (the {@link EssentialProgModelDomainBuilder}
	 * will still be used.
	 */
	public AbstractRuntimeTestCase() {
		super(new RuntimeDomainSpecifics(), null);
	}

	public AbstractRuntimeTestCase(IDomainBuilder domainBuilder) {
		super(new RuntimeDomainSpecifics(), domainBuilder);
	}

	public AbstractRuntimeTestCase(String name, IDomainBuilder domainBuilder) {
		super(name, new RuntimeDomainSpecifics(), domainBuilder);
	}
	
	protected Domain domain;
	protected SessionManager sessionManager;
	protected ISession session;
	protected IObjectStore objectStore;
	protected ITransactionManager transactionManager;
	

	protected void setUp() throws Exception {
		super.setUp();
		sessionManager = SessionManager.instance();
		domain = Domain.instance(ProgModelConstants.DEFAULT_DOMAIN_NAME);
		objectStore = new InMemoryObjectStore();
		session = sessionManager.createSession(domain, objectStore);
		transactionManager = TransactionManager.instance();
	}

	protected void tearDown() throws Exception {
		domain = null;
		sessionManager = null;
		session.reset();
		session = null;
		((InMemoryObjectStore)objectStore).reset();
		objectStore = null;
		Domain.resetAll();
		SessionManager.instance().reset();
		transactionManager.reset();
		transactionManager = null;
		super.tearDown();
	}



}
