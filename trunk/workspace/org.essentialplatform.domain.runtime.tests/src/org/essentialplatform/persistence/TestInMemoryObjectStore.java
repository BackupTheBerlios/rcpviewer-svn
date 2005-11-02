package org.essentialplatform.persistence;

import junit.framework.TestCase;
import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.IDeploymentSpecifics;
import org.essentialplatform.RuntimeDomainSpecifics;
import org.essentialplatform.domain.Domain;
import org.essentialplatform.domain.IDomain;
import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.Domain;
import org.essentialplatform.persistence.inmemory.InMemoryObjectStore;
import org.essentialplatform.progmodel.standard.ProgModelConstants;
import org.essentialplatform.session.ISession;
import org.essentialplatform.session.local.SessionManager;
import org.essentialplatform.transaction.ITransactionManager;
import org.essentialplatform.transaction.internal.TransactionManager;

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
	private final IDeploymentSpecifics domainSpecifics = new RuntimeDomainSpecifics(); 

	protected Domain domain;
	protected SessionManager sessionManager;
	protected ISession session;
	protected InMemoryObjectStore objectStore;
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
		resetAll();
		super.tearDown();
	}

	protected IDomainBuilder getDomainBuilder() {
		return domainBuilder;
	}

	protected IDomain getDomainInstance() {
		return domainSpecifics.getDomainInstance();
	}
	
	protected IDomain getDomainInstance(final String domainName) {
		return domainSpecifics.getDomainInstance(domainName);
	}

	protected <T> IDomainClass lookupAny(Class<T> domainClassIdentifier) {
		return domainSpecifics.lookupAny(domainClassIdentifier);
	}
	
	protected void resetAll() {
		domainSpecifics.resetAll();
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
