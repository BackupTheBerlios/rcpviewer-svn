package de.berlios.rcpviewer.session;

import java.util.Collection;

import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;

public class TestDomainObjectCollection extends AbstractRuntimeTestCase  {

	public TestDomainObjectCollection() {
		super(new ExtendedProgModelDomainBuilder());
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCanRemoveFromCollection() {
		IDomainClass departmentDomainClass = 
			(IDomainClass)lookupAny(Department.class);
		IDomainClass employeeDomainClass = 
			(IDomainClass) lookupAny(Employee.class);
		domain.addBuilder(getDomainBuilder());
		domain.done();
		
		IDomainObject<Department> departmentDomainObject = 
			session.recreate(departmentDomainClass);
		IDomainObject.IObjectCollectionReference employeesCollection = 
			departmentDomainObject.getCollectionReference(departmentDomainObject.getEReferenceNamed("employees"));
		IDomainObject<Employee> employeeDomainObject = 
			session.recreate(employeeDomainClass);
		
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
		IDomainClass departmentDomainClass = 
			(IDomainClass)lookupAny(Department.class);
		IDomainClass employeeDomainClass = 
			(IDomainClass)lookupAny(Employee.class);
		
		IDomainObject<Department> departmentDomainObject = 
			session.create(departmentDomainClass);
		IDomainObject.IObjectCollectionReference employeesCollection = 
			departmentDomainObject.getCollectionReference(departmentDomainObject.getEReferenceNamed("employees"));
		Collection<IDomainObject<Employee>> employees = 
			employeesCollection.getCollection();
		try {
			IDomainObject<Employee> employeeDomainObject = 
				session.create(employeeDomainClass);
			employees.add(employeeDomainObject);
			fail("Expected UnsupportedOperationException to have been thrown.");
		} catch(UnsupportedOperationException ex) {
			// expected
		}
	}

	public void testGetReferencedClassForCollection() {
		IDomainClass departmentDomainClass = 
			(IDomainClass)lookupAny(Department.class);
		IDomainClass employeeDomainClass = 
			(IDomainClass)lookupAny(Employee.class);
		
		EReference employeesCollection = departmentDomainClass.getEReferenceNamed("employees");
		assertSame(employeeDomainClass, departmentDomainClass.getReference(employeesCollection).getReferencedClass());
	}

	public void testListenersNotifiedWhenAddToCollection() {
		IDomainClass departmentDomainClass = 
			(IDomainClass)lookupAny(Department.class);
		IDomainClass employeeDomainClass = 
			(IDomainClass)lookupAny(Employee.class);
		domain.addBuilder(getDomainBuilder());
		domain.done();

		IDomainObject<Department> departmentDomainObject = 
			session.recreate(departmentDomainClass);
		IDomainObject<Employee> employeeDomainObject = 
			session.recreate(employeeDomainClass);
		IDomainObject.IObjectCollectionReference employeesCollection = 
			departmentDomainObject.getCollectionReference(departmentDomainObject.getEReferenceNamed("employees"));
		MyDomainObjectReferenceListener l =
			employeesCollection.addListener(new MyDomainObjectReferenceListener());
	
		assertFalse(l.collectionAddedToCallbackCalled);
		assertFalse(l.collectionRemovedFromCallbackCalled);
		
		employeesCollection.addToCollection(employeeDomainObject);
		
		assertTrue(l.collectionAddedToCallbackCalled);
		assertFalse(l.collectionRemovedFromCallbackCalled);
	}

	public void testListenersNotifiedWhenRemoveFromCollection() {
		IDomainClass departmentDomainClass = 
			(IDomainClass)lookupAny(Department.class);
		IDomainClass employeeDomainClass = 
			(IDomainClass)lookupAny(Employee.class);
		domain.addBuilder(getDomainBuilder());
		domain.done();

		IDomainObject<Department> departmentDomainObject = 
			session.recreate(departmentDomainClass);
		IDomainObject.IObjectCollectionReference employeesCollection = 
			departmentDomainObject.getCollectionReference(departmentDomainObject.getEReferenceNamed("employees"));
		IDomainObject<Employee> employeeDomainObject = 
			session.recreate(employeeDomainClass);
		employeesCollection.addToCollection(employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterAdd = employeesCollection.getCollection();
	
		MyDomainObjectReferenceListener l =
			employeesCollection.addListener(new MyDomainObjectReferenceListener());
	
		assertFalse(l.collectionAddedToCallbackCalled);
		assertFalse(l.collectionRemovedFromCallbackCalled);
	
		employeesCollection.removeFromCollection(employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterRemove = 
			employeesCollection.getCollection();
	
		assertFalse(l.collectionAddedToCallbackCalled);
		assertTrue(l.collectionRemovedFromCallbackCalled);
	}

	public void testCanAddToCollection() {
		IDomainClass departmentDomainClass = 
			(IDomainClass)lookupAny(Department.class);
		IDomainClass employeeDomainClass = 
			(IDomainClass)lookupAny(Employee.class);
		domain.addBuilder(getDomainBuilder());
		domain.done();

		IDomainObject<Department> departmentDomainObject = 
			session.recreate(departmentDomainClass);
		IDomainObject.IObjectCollectionReference employeesCollection = 
			departmentDomainObject.getCollectionReference(departmentDomainObject.getEReferenceNamed("employees"));
		IDomainObject<Employee> employeeDomainObject = 
			session.recreate(employeeDomainClass);
		
		Collection<IDomainObject<Employee>> employeesBeforeAdd = employeesCollection.getCollection();
		assertEquals(0, employeesBeforeAdd.size());
		employeesCollection.addToCollection(employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterAdd = employeesCollection.getCollection();
		assertEquals(1, employeesAfterAdd.size());
		assertTrue(employeesAfterAdd.contains(employeeDomainObject.getPojo()));
	}


}
