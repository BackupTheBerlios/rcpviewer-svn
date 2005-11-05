package org.essentialplatform.runtime.tests.session;

import java.util.Collection;

import org.eclipse.emf.ecore.EReference;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;
import org.essentialplatform.session.Department;
import org.essentialplatform.session.Employee;

public class TestDomainObjectCollection extends AbstractRuntimeTestCase  {

	public void testCanRemoveFromCollection() {
		IDomainClass departmentDomainClass = lookupAny(Department.class);
		IDomainClass employeeDomainClass = lookupAny(Employee.class);
		
		IDomainObject<Department> departmentDomainObject = session.recreate(departmentDomainClass);
		IDomainObject.IObjectCollectionReference employeesCollection = 
			departmentDomainObject.getCollectionReference(departmentDomainObject.getEReferenceNamed("employees"));
		IDomainObject<Employee> employeeDomainObject = session.recreate(employeeDomainClass);
		
		employeesCollection.addToCollection(employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterAdd = employeesCollection.getCollection();
		assertTrue(employeesAfterAdd.contains(employeeDomainObject));
		employeesCollection.removeFromCollection(employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterRemove = employeesCollection.getCollection();
		assertFalse(employeesAfterRemove.contains(employeeDomainObject));
	}

	/**
	 * Can get a collection; it should be immutable.
	 *
	 */
	public void testGetCollection() {
		IDomainClass departmentDomainClass = lookupAny(Department.class);
		IDomainClass employeeDomainClass = lookupAny(Employee.class);
		
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
		IDomainClass departmentDomainClass = lookupAny(Department.class);
		IDomainClass employeeDomainClass = lookupAny(Employee.class);
		
		EReference employeesCollection = departmentDomainClass.getEReferenceNamed("employees");
		assertSame(employeeDomainClass, departmentDomainClass.getIReference(employeesCollection).getReferencedDomainClass());
	}

	public void testListenersNotifiedWhenAddToCollection() {
		IDomainClass departmentDomainClass = lookupAny(Department.class);
		IDomainClass employeeDomainClass = lookupAny(Employee.class);

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
		IDomainClass departmentDomainClass = lookupAny(Department.class);
		IDomainClass employeeDomainClass = lookupAny(Employee.class);

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
		IDomainClass departmentDomainClass = lookupAny(Department.class);
		IDomainClass employeeDomainClass = lookupAny(Employee.class);

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
		assertTrue(employeesAfterAdd.contains(employeeDomainObject));
	}


}
