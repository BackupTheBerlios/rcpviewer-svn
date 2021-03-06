package org.essentialplatform.runtime.shared.tests.session;

import java.util.Collection;

import org.eclipse.emf.ecore.EReference;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.client.domain.bindings.IObjectAttributeClientBinding;
import org.essentialplatform.runtime.client.domain.bindings.IObjectCollectionReferenceClientBinding;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import org.essentialplatform.runtime.shared.tests.session.fixture.Department;
import org.essentialplatform.runtime.shared.tests.session.fixture.Employee;

public class TestDomainObjectCollection extends AbstractRuntimeClientTestCase  {

	public void testCanRemoveFromCollection() {
		IDomainClass departmentDomainClass = lookupAny(Department.class);
		IDomainClass employeeDomainClass = lookupAny(Employee.class);
		
		IDomainObject<Department> departmentDomainObject = clientSession.recreate(departmentDomainClass);
		IDomainObject.IObjectCollectionReference employeesCollection = 
			departmentDomainObject.getCollectionReference(departmentDomainObject.getIReferenceNamed("employees"));
		IDomainObject<Employee> employeeDomainObject = clientSession.recreate(employeeDomainClass);
		
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
			clientSession.create(departmentDomainClass);
		IDomainObject.IObjectCollectionReference employeesCollection = 
			departmentDomainObject.getCollectionReference(departmentDomainObject.getIReferenceNamed("employees"));
		Collection<IDomainObject<Employee>> employees = 
			employeesCollection.getCollection();
		try {
			IDomainObject<Employee> employeeDomainObject =  
				clientSession.create(employeeDomainClass);
			employees.add(employeeDomainObject);
			fail("Expected UnsupportedOperationException to have been thrown.");
		} catch(UnsupportedOperationException ex) {
			// expected
		}
	}

	public void testGetReferencedClassForCollection() {
		IDomainClass departmentDomainClass = lookupAny(Department.class);
		IDomainClass employeeDomainClass = lookupAny(Employee.class);
		
		IDomainClass.IReference employeesCollection = departmentDomainClass.getIReferenceNamed("employees");
		assertSame(employeeDomainClass, employeesCollection.getReferencedDomainClass());
	}

	public void testListenersNotifiedWhenAddToCollection() {
		IDomainClass departmentDomainClass = lookupAny(Department.class);
		IDomainClass employeeDomainClass = lookupAny(Employee.class);

		IDomainObject<Department> departmentDomainObject = 
			clientSession.recreate(departmentDomainClass);
		IDomainObject<Employee> employeeDomainObject = 
			clientSession.recreate(employeeDomainClass);
		IDomainObject.IObjectCollectionReference employeesCollection = 
			departmentDomainObject.getCollectionReference(departmentDomainObject.getIReferenceNamed("employees"));
		IObjectCollectionReferenceClientBinding refBinding = 
			(IObjectCollectionReferenceClientBinding)employeesCollection.getBinding(); 

		MyDomainObjectReferenceListener l =
			refBinding.addListener(new MyDomainObjectReferenceListener());
	
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
			clientSession.recreate(departmentDomainClass);
		IDomainObject.IObjectCollectionReference employeesCollection = 
			departmentDomainObject.getCollectionReference(departmentDomainObject.getIReferenceNamed("employees"));
		IDomainObject<Employee> employeeDomainObject = 
			clientSession.recreate(employeeDomainClass);
		employeesCollection.addToCollection(employeeDomainObject);
	
		IObjectCollectionReferenceClientBinding refBinding = 
			(IObjectCollectionReferenceClientBinding)employeesCollection.getBinding();
		
		MyDomainObjectReferenceListener l =
			refBinding.addListener(new MyDomainObjectReferenceListener());
	
		assertFalse(l.collectionAddedToCallbackCalled);
		assertFalse(l.collectionRemovedFromCallbackCalled);
	
		employeesCollection.removeFromCollection(employeeDomainObject);
	
		assertFalse(l.collectionAddedToCallbackCalled);
		assertTrue(l.collectionRemovedFromCallbackCalled);
	}

	public void testCanAddToCollection() {
		IDomainClass departmentDomainClass = lookupAny(Department.class);
		IDomainClass employeeDomainClass = lookupAny(Employee.class);

		IDomainObject<Department> departmentDomainObject = 
			clientSession.recreate(departmentDomainClass);
		IDomainObject.IObjectCollectionReference employeesCollection = 
			departmentDomainObject.getCollectionReference(departmentDomainObject.getIReferenceNamed("employees"));
		IDomainObject<Employee> employeeDomainObject = 
			clientSession.recreate(employeeDomainClass);
		
		Collection<IDomainObject<Employee>> employeesBeforeAdd = employeesCollection.getCollection();
		assertEquals(0, employeesBeforeAdd.size());
		employeesCollection.addToCollection(employeeDomainObject);
		Collection<IDomainObject<Employee>> employeesAfterAdd = employeesCollection.getCollection();
		assertEquals(1, employeesAfterAdd.size());
		assertTrue(employeesAfterAdd.contains(employeeDomainObject));
	}


}
