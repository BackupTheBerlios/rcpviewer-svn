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
import org.essentialplatform.runtime.server.domain.bindings.RuntimeServerBinding;
import org.essentialplatform.runtime.server.persistence.NoopObjectStore;
import org.essentialplatform.runtime.server.session.IServerSession;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
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
public abstract class AbstractRuntimeServerTestCase extends AbstractRuntimeSharedTestCase {

	public AbstractRuntimeServerTestCase() {
		super(null);
	}

	public AbstractRuntimeServerTestCase(IDomainBuilder domainBuilder) {
		super(domainBuilder);
	}

	public AbstractRuntimeServerTestCase(String name, IDomainBuilder domainBuilder) {
		super(name, domainBuilder);
	}

	/**
	 * Not initialized, but available for any subclass to use as it wishes.
	 */
	protected IServerSessionFactory sessionFactory;
	/**
	 * Not initialized, but available for any subclass to use as it wishes.
	 */
	protected IServerSession session;
	

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected IBinding getBinding() {
		return new RuntimeServerBinding().init(new EssentialProgModelRuntimeBuilder());
	}

	protected void tearDown() throws Exception {
		sessionFactory = null;
		session = null;
		Binding.reset();
		super.tearDown();
	}


}
