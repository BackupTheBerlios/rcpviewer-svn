package org.essentialplatform.runtime.tests;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.progmodel.standard.ProgModelConstants;
import org.essentialplatform.runtime.domain.runtime.RuntimeDeployment;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.persistence.inmemory.InMemoryObjectStore;
import org.essentialplatform.runtime.session.ISession;
import org.essentialplatform.runtime.session.local.Session;
import org.essentialplatform.runtime.session.local.SessionManager;
import org.essentialplatform.runtime.transaction.ITransactionManager;
import org.essentialplatform.runtime.transaction.internal.TransactionManager;

/**
 * Sets up a default {@link Domain}, {@link SessionManager}, 
 * {@link SessionFactory} (for this domain and with an 
 * {@link InMemoryObjectStore} and a {@link Session} for this.
 * 
 * @author Dan Haywood
 *
 */
public abstract class AbstractRuntimeTestCase extends AbstractTestCase {

	public AbstractRuntimeTestCase() {
		super(null);
	}

	public AbstractRuntimeTestCase(IDomainBuilder domainBuilder) {
		super(domainBuilder);
	}

	public AbstractRuntimeTestCase(String name, IDomainBuilder domainBuilder) {
		super(name, domainBuilder);
	}

	protected Domain domain;
	protected SessionManager sessionManager;
	protected ISession session;
	protected IObjectStore objectStore;
	protected ITransactionManager transactionManager;
	

	protected void setUp() throws Exception {
		super.setUp();
		new RuntimeDeployment();
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
		Deployment.reset();
		super.tearDown();
	}



}
