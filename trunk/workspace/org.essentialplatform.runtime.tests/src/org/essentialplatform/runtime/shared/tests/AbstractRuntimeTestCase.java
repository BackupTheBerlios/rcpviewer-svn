package org.essentialplatform.runtime.shared.tests;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.tests.AbstractTestCase;
import org.essentialplatform.progmodel.essential.app.ProgModelConstants;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.client.RuntimeClientBinding;
import org.essentialplatform.runtime.client.session.ClientSession;
import org.essentialplatform.runtime.client.session.ClientSessionManager;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.shared.persistence.IObjectStore;
import org.essentialplatform.runtime.shared.persistence.NoopObjectStore;
import org.essentialplatform.runtime.shared.persistence.PersistenceConstants;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.transaction.ITransactionManager;
import org.essentialplatform.runtime.shared.transaction.TransactionManager;

/**
 * Sets up a default {@link Domain}, {@link ClientSessionManager}, 
 * {@link SessionFactory} (for this domain and with an 
 * {@link NoopObjectStore} and a {@link ClientSession} for this.
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
	protected ClientSessionManager sessionManager;
	protected IClientSession session;
	protected ITransactionManager transactionManager;
	

	protected void setUp() throws Exception {
		super.setUp();
		Binding.setBinding(
			new RuntimeClientBinding(new EssentialProgModelRuntimeBuilder()));
		sessionManager = ClientSessionManager.instance();
		domain = Domain.instance(ProgModelConstants.DEFAULT_DOMAIN_NAME);
		session = sessionManager.defineSession(
				new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID));
		transactionManager = TransactionManager.instance();
	}

	protected void tearDown() throws Exception {
		domain = null;
		sessionManager = null;
		session.reset();
		session = null;
		Domain.resetAll();
		ClientSessionManager.instance().reset();
		transactionManager.reset();
		transactionManager = null;
		Binding.reset();
		super.tearDown();
	}



}
