package de.berlios.rcpviewer.session;

import java.util.Collection;

import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.RuntimeDomain;
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
		IRuntimeDomainClass<Department> departmentDomainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		IRuntimeDomainClass<Employee> employeeDomainClass = 
			(IRuntimeDomainClass<Employee>)lookupAny(Employee.class);
		
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

	public void testCanAddToCollection() {
		IRuntimeDomainClass<Department> departmentDomainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		IRuntimeDomainClass<Employee> employeeDomainClass = 
			(IRuntimeDomainClass<Employee>)lookupAny(Employee.class);
		
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


}
