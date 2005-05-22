package de.berlios.rcpviewer.session;

import java.util.Collection;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.Department;
import de.berlios.rcpviewer.domain.Employee;
import de.berlios.rcpviewer.session.local.Session;

public class TestDomainObject extends AbstractTestCase  {

	private static class MyDomainObjectListener implements IDomainObjectListener {
		boolean attributeChangedCallbackCalled = false;
		public void attributeChanged(DomainObjectAttributeEvent event) {
			attributeChangedCallbackCalled=true;
		}

		boolean persistedCallbackCalled = false;
		public void persisted(DomainObjectEvent event) {
			persistedCallbackCalled=true;
		}

		boolean collectionAddedToCallbackCalled = false;
		public void collectionAddedTo(DomainObjectReferenceEvent event) {
			collectionAddedToCallbackCalled=true;
		}
		
		boolean collectionRemovedFromCallbackCalled = false;
		public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
			collectionRemovedFromCallbackCalled=true;
		}

	}

	private ISession session;
	private Domain domain;
	protected void setUp() throws Exception {
		super.setUp();
		session = new Session();
	}

	protected void tearDown() throws Exception {
		Domain.reset();
		domain = null;
		session = null;
		super.tearDown();
	}


	
	/**
	 * 
	 */
	public void testCanPersistThroughDomainObject() {
		session = Session.instance();
		IDomainClass<Department> domainClass = 
			Domain.lookup(Department.class);
		Domain.instance().done();
		
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
	 * In the standard programming model, we require that the Session is
	 * asked to persist. 
	 * 
	 * <p>
	 * TODO: the original design was to have an aspect pick up on the 'save'
	 * method.
	 */
	public void incompletetestCanPersistThroughPojo() {
		session = Session.instance(); // must use Singleton since this is what Aspect uses.
		IDomainClass<Department> domainClass = 
			Domain.lookup(Department.class);
		Domain.instance().done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		session.persist(domainObject.getPojo());
		assertTrue(domainObject.isPersistent());
	}

	/**
	 * Create directly from DomainClass rather than from Session. 
	 */
	public void testCannotPersistIfNotAttachedToSession() {
		IDomainClass<Department> domainClass = 
			Domain.lookup(Department.class);
		Domain.instance().done();
		
		IDomainObject<Department> domainObject = domainClass.createTransient();
		assertFalse(session.isAttached(domainObject));
		try {
			domainObject.persist();
			fail("IllegalStateException should have been thrown.");
		} catch(IllegalStateException ex) {
			// expected
		}
	}


	public void testCannotPersistMoreThanOnce() {
		session = Session.instance(); // since Aspect will use singleton Session
		IDomainClass<Department> domainClass = 
			Domain.lookup(Department.class);
		Domain.instance().done();
		
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
			Domain.lookup(Department.class);
		Domain.instance().done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		domainObject.set(nameAttribute, "HR");
		assertEquals("HR", domainObject.getPojo().getName());
	}

	public void testCannotSetAttributeToInvalidValue() {
		IDomainClass<Department> domainClass = 
			Domain.lookup(Department.class);
		Domain.instance().done();
		
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
			Domain.lookup(Department.class);
		Domain.instance().done();
		
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
			Domain.lookup(Department.class);
		Domain.instance().done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		domainObject.getPojo().setName("HR");
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		String value = (String)domainObject.get(nameAttribute);
		assertEquals("HR", value);
	}
	
	public void testCanInvokeOperation() {
		IDomainClass<Department> domainClass = 
			Domain.lookup(Department.class);
		Domain.instance().done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		Department pojo = domainObject.getPojo();
		EOperation moveOfficeOperation = domainObject.getEOperationNamed("moveOffice");
		assertFalse(pojo.movedOffice);
		domainObject.invokeOperation(moveOfficeOperation, new Object[]{});
		assertTrue(pojo.movedOffice);
	}

	
	/**
	 * Can get a collection; it should be immutable.
	 *
	 */
	public void testGetCollection() {
		IDomainClass<Department> departmentDomainClass = 
			Domain.lookup(Department.class);
		IDomainClass<Employee> employeeDomainClass = 
			Domain.lookup(Employee.class);
		Domain.instance().done();
		
		IDomainObject<Department> departmentDomainObject = 
			session.createTransient(departmentDomainClass);
		EReference employeesCollection = departmentDomainObject.getEReferenceNamed("employees");
		Collection<IDomainObject<Employee>> employees = 
			departmentDomainObject.getCollection(employeesCollection);
		try {
			IDomainObject<Employee> employeeDomainObject = 
				session.createTransient(employeeDomainClass);
			employees.add(employeeDomainObject);
			fail("Expected UnsupportedOperationException to have been thrown.");
		} catch(UnsupportedOperationException ex) {
			// expected
		}
	}

	public void testGetReferencedClassForCollection() {
		IDomainClass<Department> departmentDomainClass = Domain.lookup(Department.class);
		IDomainClass<Employee> employeeDomainClass = Domain.lookup(Employee.class);
		Domain.instance().done();
		
		EReference employeesCollection = departmentDomainClass.getEReferenceNamed("employees");
		assertSame(employeeDomainClass, departmentDomainClass.getReferencedClass(employeesCollection));
	}


	public void testCanAddToCollection() {
		IDomainClass<Department> departmentDomainClass = 
			Domain.lookup(Department.class);
		IDomainClass<Employee> employeeDomainClass = 
			Domain.lookup(Employee.class);
		Domain.instance().done();
		
		IDomainObject<Department> departmentDomainObject = 
			session.createTransient(departmentDomainClass);
		EReference employeesCollection = departmentDomainObject.getEReferenceNamed("employees");
		IDomainObject<Employee> employeeDomainObject = 
			session.createTransient(employeeDomainClass);
		Collection<IDomainObject<Employee>> employeesBeforeAdd = departmentDomainObject.getCollection(employeesCollection);
		assertEquals(0, employeesBeforeAdd.size());
		departmentDomainObject.addToCollection(employeesCollection, employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterAdd = departmentDomainObject.getCollection(employeesCollection);
		assertEquals(1, employeesAfterAdd.size());
		assertTrue(employeesAfterAdd.contains(employeeDomainObject.getPojo()));
	}


	public void testCanRemoveFromCollection() {
		IDomainClass<Department> departmentDomainClass = 
			Domain.lookup(Department.class);
		IDomainClass<Employee> employeeDomainClass = 
			Domain.lookup(Employee.class);
		Domain.instance().done();
		
		IDomainObject<Department> departmentDomainObject = 
			session.createTransient(departmentDomainClass);
		EReference employeesCollection = departmentDomainObject.getEReferenceNamed("employees");
		IDomainObject<Employee> employeeDomainObject = 
			session.createTransient(employeeDomainClass);
		departmentDomainObject.addToCollection(employeesCollection, employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterAdd = departmentDomainObject.getCollection(employeesCollection);
		assertTrue(employeesAfterAdd.contains(employeeDomainObject.getPojo()));
		departmentDomainObject.removeFromCollection(employeesCollection, employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterRemove = departmentDomainObject.getCollection(employeesCollection);
		assertFalse(employeesAfterRemove.contains(employeeDomainObject.getPojo()));
	}
	
	public void incompletetestCanAssociateToSingleReference() {
		
	}

	public void incompletetestCanDissociateToSingleReference() {
		
	}

	public void testListenersNotifiedWhenAddToCollection() {
		IDomainClass<Department> departmentDomainClass = 
			Domain.lookup(Department.class);
		IDomainClass<Employee> employeeDomainClass = 
			Domain.lookup(Employee.class);
		Domain.instance().done();
		
		IDomainObject<Department> departmentDomainObject = 
			session.createTransient(departmentDomainClass);
		MyDomainObjectListener l =
			departmentDomainObject.addDomainObjectListener(new MyDomainObjectListener());

		assertFalse(l.collectionAddedToCallbackCalled);
		assertFalse(l.collectionRemovedFromCallbackCalled);
		
		IDomainObject<Employee> employeeDomainObject = 
			session.createTransient(employeeDomainClass);
		EReference employeesCollection = departmentDomainObject.getEReferenceNamed("employees");
		departmentDomainObject.addToCollection(employeesCollection, employeeDomainObject);
		
		assertTrue(l.collectionAddedToCallbackCalled);
		assertFalse(l.collectionRemovedFromCallbackCalled);
	}

	public void testListenersNotifiedWhenRemoveFromCollection() {
		IDomainClass<Department> departmentDomainClass = 
			Domain.lookup(Department.class);
		IDomainClass<Employee> employeeDomainClass = 
			Domain.lookup(Employee.class);
		Domain.instance().done();
		
		IDomainObject<Department> departmentDomainObject = 
			session.createTransient(departmentDomainClass);
		EReference employeesCollection = departmentDomainObject.getEReferenceNamed("employees");
		IDomainObject<Employee> employeeDomainObject = 
			session.createTransient(employeeDomainClass);
		departmentDomainObject.addToCollection(employeesCollection, employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterAdd = departmentDomainObject.getCollection(employeesCollection);

		MyDomainObjectListener l =
			departmentDomainObject.addDomainObjectListener(new MyDomainObjectListener());

		assertFalse(l.collectionAddedToCallbackCalled);
		assertFalse(l.collectionRemovedFromCallbackCalled);

		departmentDomainObject.removeFromCollection(employeesCollection, employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterRemove = departmentDomainObject.getCollection(employeesCollection);

		assertFalse(l.collectionAddedToCallbackCalled);
		assertTrue(l.collectionRemovedFromCallbackCalled);
	}

	public void incompletetestListenersNotifiedWhenAssociateSingleReference() {
		
	}

	public void incompletetestListenersNotifiedWhenDissociateSingleReference() {
		
	}


}
