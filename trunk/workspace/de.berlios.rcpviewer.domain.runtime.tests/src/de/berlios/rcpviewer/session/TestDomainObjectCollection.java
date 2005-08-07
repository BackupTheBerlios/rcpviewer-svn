package de.berlios.rcpviewer.session;

import java.util.Collection;

import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;

public class TestDomainObjectCollection extends AbstractRuntimeTestCase  {

	public TestDomainObjectCollection() {
		super(null);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCanRemoveFromCollection() {
		IRuntimeDomainClass<Department> departmentDomainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		IRuntimeDomainClass<Employee> employeeDomainClass = 
			(IRuntimeDomainClass<Employee>) lookupAny(Employee.class);
		
		IDomainObject<Department> departmentDomainObject = 
			session.createTransient(departmentDomainClass);
		IDomainObject.IReference employeesCollection = departmentDomainObject.getReference(departmentDomainObject.getEReferenceNamed("employees"));
		IDomainObject<Employee> employeeDomainObject = 
			session.createTransient(employeeDomainClass);
		employeesCollection.addToCollection(employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterAdd = employeesCollection.getCollection();
		assertTrue(employeesAfterAdd.contains(employeeDomainObject.getPojo()));
		employeesCollection.removeFromCollection(employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterRemove = employeesCollection.getCollection();
		assertFalse(employeesAfterRemove.contains(employeeDomainObject.getPojo()));
	}

	/**
	 * Can get a collection; it should be immutable.
	 *
	 */
	public void testGetCollection() {
		IRuntimeDomainClass<Department> departmentDomainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		IRuntimeDomainClass<Employee> employeeDomainClass = 
			(IRuntimeDomainClass<Employee>)lookupAny(Employee.class);
		
		IDomainObject<Department> departmentDomainObject = 
			session.createTransient(departmentDomainClass);
		IDomainObject.IReference employeesCollection = departmentDomainObject.getReference(departmentDomainObject.getEReferenceNamed("employees"));
		Collection<IDomainObject<Employee>> employees = 
			employeesCollection.getCollection();
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
		IDomainClass<Department> departmentDomainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		IDomainClass<Employee> employeeDomainClass = 
			(IRuntimeDomainClass<Employee>)lookupAny(Employee.class);
		
		EReference employeesCollection = departmentDomainClass.getEReferenceNamed("employees");
		assertSame(employeeDomainClass, departmentDomainClass.getReferencedClass(employeesCollection));
	}

	public void testListenersNotifiedWhenAddToCollection() {
		IRuntimeDomainClass<Department> departmentDomainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		IRuntimeDomainClass<Employee> employeeDomainClass = 
			(IRuntimeDomainClass<Employee>)lookupAny(Employee.class);
		
		IDomainObject<Department> departmentDomainObject = 
			session.createTransient(departmentDomainClass);
		IDomainObject<Employee> employeeDomainObject = 
			session.createTransient(employeeDomainClass);
		IDomainObject.IReference employeesCollection = departmentDomainObject.getReference(departmentDomainObject.getEReferenceNamed("employees"));
		MyDomainObjectReferenceListener l =
			employeesCollection.addDomainObjectReferenceListener(new MyDomainObjectReferenceListener());
	
		assertFalse(l.collectionAddedToCallbackCalled);
		assertFalse(l.collectionRemovedFromCallbackCalled);
		

		
		employeesCollection.addToCollection(employeeDomainObject);
		
		assertTrue(l.collectionAddedToCallbackCalled);
		assertFalse(l.collectionRemovedFromCallbackCalled);
	}

	public void testListenersNotifiedWhenRemoveFromCollection() {
		IRuntimeDomainClass<Department> departmentDomainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		IRuntimeDomainClass<Employee> employeeDomainClass = 
			(IRuntimeDomainClass<Employee>)lookupAny(Employee.class);
		
		IDomainObject<Department> departmentDomainObject = 
			session.createTransient(departmentDomainClass);
		IDomainObject.IReference employeesCollection = departmentDomainObject.getReference(departmentDomainObject.getEReferenceNamed("employees"));
		IDomainObject<Employee> employeeDomainObject = 
			session.createTransient(employeeDomainClass);
		employeesCollection.addToCollection(employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterAdd = employeesCollection.getCollection();
	
		MyDomainObjectReferenceListener l =
			employeesCollection.addDomainObjectReferenceListener(new MyDomainObjectReferenceListener());
	
		assertFalse(l.collectionAddedToCallbackCalled);
		assertFalse(l.collectionRemovedFromCallbackCalled);
	
		employeesCollection.removeFromCollection(employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterRemove = employeesCollection.getCollection();
	
		assertFalse(l.collectionAddedToCallbackCalled);
		assertTrue(l.collectionRemovedFromCallbackCalled);
	}

	public void testCanAddToCollection() {
		IRuntimeDomainClass<Department> departmentDomainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		IRuntimeDomainClass<Employee> employeeDomainClass = 
			(IRuntimeDomainClass<Employee>)lookupAny(Employee.class);
		
		IDomainObject<Department> departmentDomainObject = 
			session.createTransient(departmentDomainClass);
		IDomainObject.IReference employeesCollection = departmentDomainObject.getReference(departmentDomainObject.getEReferenceNamed("employees"));
		IDomainObject<Employee> employeeDomainObject = 
			session.createTransient(employeeDomainClass);
		Collection<IDomainObject<Employee>> employeesBeforeAdd = employeesCollection.getCollection();
		assertEquals(0, employeesBeforeAdd.size());
		employeesCollection.addToCollection(employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterAdd = employeesCollection.getCollection();
		assertEquals(1, employeesAfterAdd.size());
		assertTrue(employeesAfterAdd.contains(employeeDomainObject.getPojo()));
	}


}
