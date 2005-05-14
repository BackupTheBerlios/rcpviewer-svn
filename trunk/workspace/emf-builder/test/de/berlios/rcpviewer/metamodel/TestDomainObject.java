package de.berlios.rcpviewer.metamodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.progmodel.standard.impl.Department;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.Session;

public class TestDomainObject extends AbstractTestCase  {

	private static class MyDomainObjectListener implements IDomainObjectListener {
		boolean attributeChangedCallbackCalled = false;
		boolean persistedCallbackCalled = false;
		public void attributeChanged(DomainObjectAttributeEvent event) {
			attributeChangedCallbackCalled=true;
		}
		public void persisted(DomainObjectEvent event) {
			persistedCallbackCalled=true;
		}
	}

	private ISession session;
	private MetaModel metaModel;
	/**
	 * Need to use MetaModel on the thread because the aspect that creates the
	 * IDomainObject uses that metamodel for its lookup.
	 *
	 */
	protected void setUp() throws Exception {
		super.setUp();
		//metaModel = new MetaModel();
		metaModel = MetaModel.threadInstance();
		session = new Session();
	}

	protected void tearDown() throws Exception {
		metaModel.reset();
		metaModel = null;
		session = null;
		super.tearDown();
	}

	
	/**
	 * 
	 */
	public void testCanPersistThroughDomainObject() {
		session = Session.instance(); // use singleton since this is what Aspect
		IDomainClass<Department> domainClass = 
			metaModel.lookup(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		domainObject.persist();
		assertTrue(domainObject.isPersistent());
	}

	/**
	 * Depending upon the programming model, the pojo may request that it is
	 * persisted.
	 * 
	 * <p>
	 * The means for doing this will be dependent on the programming model.
	 * In the standard programming model, we pick up on a method called 'save'.
	 */
	public void testCanPersistThroughPojo() {
		session = Session.instance(); // must use Singleton since this is what Aspect uses.
		IDomainClass<Department> domainClass = 
			metaModel.lookup(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		domainObject.getPojo().save();
		assertTrue(domainObject.isPersistent());
	}

	/**
	 * Create directly from DomainClass rather than from Session. 
	 */
	public void testCannotPersistIfNotAttachedToSession() {
		IDomainClass<Department> domainClass = 
			metaModel.lookup(Department.class);
		IDomainObject<Department> domainObject = domainClass.createTransient();
		assertFalse(session.isAttached(domainObject));
		try {
			domainObject.persist();
			fail("IllegalArgumentException should have been thrown.");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}


	public void testCannotPersistMoreThanOnce() {
		session = Session.instance(); // since Aspect will use singleton Session
		IDomainClass<Department> domainClass = 
			metaModel.lookup(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		domainObject.persist();
		try {
			domainObject.persist();
			fail("IllegalStateException should have been thrown.");
		} catch(IllegalStateException ex) {
			// expected
		}
	}


	public void testCanSetAttribute() {
		IDomainClass<Department> domainClass = 
			metaModel.lookup(Department.class);
		metaModel.done();
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		domainObject.set(nameAttribute, "HR");
		assertEquals("HR", domainObject.getPojo().getName());
	}

	public void testCannotSetAttributeToInvalidValue() {
		IDomainClass<Department> domainClass = 
			metaModel.lookup(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		try {
			domainObject.set(nameAttribute, new Integer(1));
			fail("Expected IllegalArgumentException to have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected.
		}
	}

	public void testSettingAttributeNotifiesListeners() {
		IDomainClass<Department> domainClass = 
			metaModel.lookup(Department.class);
		metaModel.done();
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		MyDomainObjectListener l =
			domainObject.addDomainObjectListener(new MyDomainObjectListener());
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		domainObject.set(nameAttribute, "HR");
		assertTrue(l.attributeChangedCallbackCalled);
		assertFalse(l.persistedCallbackCalled);
	}


	public void testCanGetAttribute() {
		IDomainClass<Department> domainClass = 
			metaModel.lookup(Department.class);
		metaModel.done();
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		domainObject.getPojo().setName("HR");
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		String value = (String)domainObject.get(nameAttribute);
		assertEquals("HR", value);
	}
	
	public void testCanInvokeOperation() {
		
		IDomainClass<Department> domainClass = 
			metaModel.lookup(Department.class);
		metaModel.done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		Department pojo = domainObject.getPojo();
		EOperation moveOfficeOperation = domainObject.getEOperationNamed("moveOffice");
		assertFalse(pojo.movedOffice);
		domainObject.invokeOperation(moveOfficeOperation, new Object[]{});
		assertTrue(pojo.movedOffice);
	}


}
