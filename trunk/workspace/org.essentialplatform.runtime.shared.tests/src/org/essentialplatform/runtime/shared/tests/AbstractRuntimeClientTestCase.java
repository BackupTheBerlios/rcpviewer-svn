package org.essentialplatform.runtime.shared.tests;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.deployment.IBinding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.tests.AbstractTestCase;
import org.essentialplatform.progmodel.essential.app.ProgModelConstants;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;
import org.essentialplatform.runtime.client.session.ClientSession;
import org.essentialplatform.runtime.client.session.ClientSessionManager;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.client.transaction.ITransactionManager;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.essentialplatform.runtime.server.persistence.NoopObjectStore;
import org.essentialplatform.runtime.shared.persistence.PersistenceConstants;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Sets up a default {@link Domain}, {@link ClientSessionManager}, 
 * {@link SessionFactory} (for this domain and with an 
 * {@link NoopObjectStore} and a {@link ClientSession} for this.
 * 
 * @author Dan Haywood
 *
 */
public abstract class AbstractRuntimeClientTestCase extends AbstractRuntimeSharedTestCase {

	public AbstractRuntimeClientTestCase() {
		super();
	}

	public AbstractRuntimeClientTestCase(IDomainBuilder domainBuilder) {
		super(domainBuilder);
	}

	public AbstractRuntimeClientTestCase(String name, IDomainBuilder domainBuilder) {
		super(name, domainBuilder);
	}

	protected ClientSessionManager clientSessionManager;
	protected IClientSession clientSession;
	protected ITransactionManager transactionManager;
	

	protected void setUp() throws Exception {
		super.setUp();
		clientSessionManager = ClientSessionManager.instance();
		clientSession = clientSessionManager.defineSession(
				new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID));
		transactionManager = TransactionManager.instance();
	}

	/**
	 * Requires domainBuilder to have been specified (defaults to
	 * {@link EssentialProgModelRuntimeBuilder}.
	 */
	protected IBinding getBinding() {
		return new RuntimeClientBinding().initPrimaryBuilder(getDomainBuilder());
	}


	protected void tearDown() throws Exception {
		clientSessionManager = null;
		clientSession.reset();
		clientSession = null;
		ClientSessionManager.instance().reset();
		transactionManager.reset();
		transactionManager = null;
		Binding.reset();
		super.tearDown();
	}



}
