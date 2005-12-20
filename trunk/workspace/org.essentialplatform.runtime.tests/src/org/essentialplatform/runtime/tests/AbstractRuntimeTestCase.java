package org.essentialplatform.runtime.tests;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.tests.AbstractTestCase;
import org.essentialplatform.progmodel.essential.app.ProgModelConstants;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.RuntimeBinding;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.persistence.NoopObjectStore;
import org.essentialplatform.runtime.session.ISession;
import org.essentialplatform.runtime.session.Session;
import org.essentialplatform.runtime.session.SessionManager;
import org.essentialplatform.runtime.transaction.ITransactionManager;
import org.essentialplatform.runtime.transaction.TransactionManager;

/**
 * Sets up a default {@link Domain}, {@link SessionManager}, 
 * {@link SessionFactory} (for this domain and with an 
 * {@link NoopObjectStore} and a {@link Session} for this.
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
		Binding.setBinding(
			new RuntimeBinding(new EssentialProgModelRuntimeBuilder()));
		sessionManager = SessionManager.instance();
		domain = Domain.instance(ProgModelConstants.DEFAULT_DOMAIN_NAME);
		objectStore = new NoopObjectStore();
		session = sessionManager.createSession(domain, objectStore);
		transactionManager = TransactionManager.instance();
	}

	protected void tearDown() throws Exception {
		domain = null;
		sessionManager = null;
		session.reset();
		session = null;
		((NoopObjectStore)objectStore).reset();
		objectStore = null;
		Domain.resetAll();
		SessionManager.instance().reset();
		transactionManager.reset();
		transactionManager = null;
		Binding.reset();
		super.tearDown();
	}



}
