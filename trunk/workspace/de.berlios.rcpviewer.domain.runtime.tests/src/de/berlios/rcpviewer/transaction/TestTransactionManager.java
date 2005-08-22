package de.berlios.rcpviewer.transaction;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import junit.framework.TestCase;

public class TestTransactionManager extends TestCase {

	protected RuntimeDomain domain;
	protected SessionManager sessionManager;
	protected ISession session;
	protected IObjectStore objectStore;
	
	public TestTransactionManager() {
		super(null);
	}

	protected void setUp() throws Exception {
		super.setUp();
		sessionManager = SessionManager.instance();
		domain = RuntimeDomain.instance(ProgModelConstants.DEFAULT_DOMAIN_NAME);
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
		RuntimeDomain.resetAll();
		SessionManager.instance().reset();
		super.tearDown();
	}

	public void testInvokeOperationDirectly() {
		IRuntimeDomainClass<Calculator> domainClass = 
			(IRuntimeDomainClass<Calculator>)RuntimeDomain.lookupAny(Calculator.class);
		domain.addBuilder(new ExtendedProgModelDomainBuilder());
		domain.done();
		
		IDomainObject<Calculator> domainObject = 
			(IDomainObject<Calculator>)session.createTransient(domainClass);
		Calculator calculator = domainObject.getPojo();
		
		assertEquals(0, calculator.getResult());

		calculator.add(5);

	}
	
	public void testInvokeOperationViaDomainObject() {

		IRuntimeDomainClass<Calculator> domainClass = 
			(IRuntimeDomainClass<Calculator>)RuntimeDomain.lookupAny(Calculator.class);
		domain.addBuilder(new ExtendedProgModelDomainBuilder());
		domain.done();
		
		IDomainObject<Calculator> domainObject = 
			(IDomainObject<Calculator>)session.createTransient(domainClass);
		Calculator calculator = domainObject.getPojo();
		
		assertEquals(0, calculator.getResult());

		EOperation addEOperation = domainObject.getEOperationNamed("add");
		domainObject.getOperation(addEOperation).invokeOperation(new Object[]{new Integer(5)});

	}

	public void testTransationScope() {
		IRuntimeDomainClass<Calculator> domainClass = 
			(IRuntimeDomainClass<Calculator>)RuntimeDomain.lookupAny(Calculator.class);
		domain.addBuilder(new ExtendedProgModelDomainBuilder());
		domain.done();
		
		IDomainObject<Calculator> domainObject = 
			(IDomainObject<Calculator>)session.createTransient(domainClass);
		Calculator calculator = domainObject.getPojo();
		
		assertEquals(0, calculator.getResult());

		calculator.computeFactorial(5);

	}

}
