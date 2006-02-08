package org.essentialplatform.server.tests.persistence.inmemory;

import junit.framework.TestCase;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.progmodel.essential.app.ProgModelConstants;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;
import org.essentialplatform.runtime.client.session.ClientSessionManager;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.client.transaction.ITransactionManager;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.essentialplatform.runtime.server.persistence.inmemory.InMemoryObjectStore;

/**
 * Have copied down stuff from subclass; need to simplify.
 * 
 * @author Dan Haywood
 */
public class TestInMemoryObjectStore extends TestCase {

	public TestInMemoryObjectStore() {
		super(null);
	}

	private final IDomainBuilder domainBuilder = null;

	protected Domain domain;
	protected ClientSessionManager sessionManager;
	protected IClientSession session;
	protected InMemoryObjectStore objectStore;
	protected ITransactionManager transactionManager;


	protected void setUp() throws Exception {
		super.setUp();
		Binding.setBinding(
				new RuntimeClientBinding().init(new EssentialProgModelRuntimeBuilder()));
		sessionManager = ClientSessionManager.instance();
		domain = Domain.instance(ProgModelConstants.DEFAULT_DOMAIN_NAME);
		//objectStore = new InMemoryObjectStore();
		//session = sessionManager.defineSession(domain, objectStore);
		transactionManager = TransactionManager.instance();
	}

	protected void tearDown() throws Exception {
		resetAll();
		Binding.reset();
		super.tearDown();
	}

	protected IDomainBuilder getDomainBuilder() {
		return domainBuilder;
	}

	protected IDomain getDomainInstance() {
		return Domain.instance();
	}
	
	protected IDomain getDomainInstance(final String domainName) {
		return Domain.instance(domainName);
	}

	protected <T> IDomainClass lookupAny(Class<T> domainClassIdentifier) {
		return Domain.lookupAny(domainClassIdentifier);
	}
	
	protected void resetAll() {
		Domain.resetAll();
	}
	
	
	
	public void testDummy() {
	}
	
	public void incompletetestReset() {
		objectStore.reset();
	}
	
	public void incompletetestCanPersistObjectThatHasntBeen() {
		//objectStore.persist(domainObject);
	}
	
	/**
	 * With an entity integrity exception.
	 *
	 */
	public void incompletetestSaveObjectFailsIfAlreadyPersisted() {
		//objectStore.save(domainObject);
	}
	
	public void incompletetestCanUpdateObjectThatHasBeenPersisted() {
		//objectStore.update(domainObject);
	}
	
	public void incompletetestUpdateObjectFailsForObjectThatIsNotAlreadyPersisted() {
		//objectStore.update(domainObject);
	}
	
	public void incompletetestSaveObjectFailsIfConcurrencyException() {
		
	}

	public void incompletetestAllInstancesWhenNone() {
		//objectStore.allInstances();
	}
}
