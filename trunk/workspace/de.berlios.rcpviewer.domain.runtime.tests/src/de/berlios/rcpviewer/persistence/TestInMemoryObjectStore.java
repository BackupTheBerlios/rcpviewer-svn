package de.berlios.rcpviewer.persistence;

import junit.framework.TestCase;
import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.transaction.ITransactionManager;
import de.berlios.rcpviewer.transaction.internal.TransactionManager;

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

	protected RuntimeDomain domain;
	protected SessionManager sessionManager;
	protected ISession session;
	protected IObjectStore objectStore;
	protected ITransactionManager transactionManager;


	protected void setUp() throws Exception {
		super.setUp();
		sessionManager = SessionManager.instance();
		domain = RuntimeDomain.instance(ProgModelConstants.DEFAULT_DOMAIN_NAME);
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

	protected <T> IDomainClass<T> lookupAny(Class<T> domainClassIdentifier) {
		return domainSpecifics.lookupAny(domainClassIdentifier);
	}
	
	protected void resetAll() {
		domainSpecifics.resetAll();
	}
	
	
	
	public void testDummy() {
	}
	
	public void incompletetestCanPersistObjectThatHasntBeen() {
		
	}
	
	public void incompletetestAttemptingToPersistAnObjectThatIsAlreadyPersistedFails() {
		
	}
	
	public void incompletetestCanSaveObjectThatHasBeenPersisted() {
		
	}
	
	public void incompletetestAttemptingToSaveObjectThatIsNotAlreadyPersisted() {
		
	}
	

}
