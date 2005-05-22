package de.berlios.rcpviewer.progmodel.standard;


import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;

public class TestDomainClassReferences extends AbstractTestCase {

	private Domain domain;
	private IDomainClass<?> departmentDomainClass;
	private IDomainClass<?> employeeDomainClass;
	private IDomainClass<TestDomainClassReferencesName> nameDomainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		Domain.reset();
		domain = null;
		departmentDomainClass = null;
		employeeDomainClass = null;
		nameDomainClass = null;
		super.tearDown();
	}
	

	public void testOneToManyIsPickedUp(){
		departmentDomainClass = Domain.lookup(TestDomainClassReferencesDepartment.class);
		employeeDomainClass = Domain.lookup(TestDomainClassReferencesEmployee.class);
		Domain.instance().done();
		
		assertEquals(1, departmentDomainClass.references().size());
		EReference refToEmployees = departmentDomainClass.references().get(0);
		assertEquals("employees", refToEmployees.getName());
		assertSame(employeeDomainClass, departmentDomainClass.getReferencedClass(refToEmployees));
		assertTrue(departmentDomainClass.isMultiple(refToEmployees));
		assertFalse(departmentDomainClass.isOrdered(refToEmployees));
		assertFalse(departmentDomainClass.isContainer(refToEmployees));
		assertTrue(departmentDomainClass.isUnique(refToEmployees));
		assertFalse(departmentDomainClass.isChangeable(refToEmployees));
		assertFalse(departmentDomainClass.isDerived(refToEmployees));
	} 

	public void testOneToOneIsPickedUp(){
		departmentDomainClass = Domain.lookup(TestDomainClassReferencesDepartment.class);
		employeeDomainClass = Domain.lookup(TestDomainClassReferencesEmployee.class);
		Domain.instance().done();
		
		assertEquals(1, employeeDomainClass.references().size());
		EReference refToDepartment = employeeDomainClass.references().get(0);
		assertEquals("department", refToDepartment.getName());
		assertSame(departmentDomainClass, employeeDomainClass.getReferencedClass(refToDepartment));
		assertFalse(employeeDomainClass.isMultiple(refToDepartment));
		assertFalse(employeeDomainClass.isOrdered(refToDepartment));
		assertFalse(employeeDomainClass.isContainer(refToDepartment));
		assertFalse(employeeDomainClass.isUnique(refToDepartment));
		assertFalse(employeeDomainClass.isChangeable(refToDepartment));
		assertFalse(employeeDomainClass.isDerived(refToDepartment));
	} 

	public void testImmutableOneToManyIsPickedUp(){
		departmentDomainClass = Domain.lookup(TestDomainClassReferencesDepartmentImmutableEmployeeCollection.class);
		employeeDomainClass = Domain.lookup(TestDomainClassReferencesEmployee.class);
		Domain.instance().done();
		
		assertEquals(1, departmentDomainClass.references().size());
		EReference refToEmployees = departmentDomainClass.references().get(0);
		assertEquals("employees", refToEmployees.getName());
		assertFalse(departmentDomainClass.isChangeable(refToEmployees));
	} 

	public void testImmutableOneToOneIsPickedUp(){
		employeeDomainClass = Domain.lookup(TestDomainClassReferencesEmployeeImmutableNameRef.class);
		nameDomainClass = Domain.lookup(TestDomainClassReferencesName.class);
		Domain.instance().done();
		
		assertEquals(1, employeeDomainClass.references().size());
		EReference refToName = employeeDomainClass.references().get(0);
		assertEquals("name", refToName.getName());
		assertSame(nameDomainClass, employeeDomainClass.getReferencedClass(refToName));
		assertFalse(employeeDomainClass.isChangeable(refToName));
	} 

	public void testDerivedOneToManyIsPickedUp(){
		departmentDomainClass = Domain.lookup(TestDomainClassReferencesDepartmentDerivedReferences.class);
		Domain.instance().done();
		
		EReference derivedRefToEmployees = departmentDomainClass.getEReferenceNamed("terminatedEmployees");
		assertEquals("terminatedEmployees", derivedRefToEmployees.getName());
		assertTrue(departmentDomainClass.isMultiple(derivedRefToEmployees));
		assertTrue(departmentDomainClass.isDerived(derivedRefToEmployees));
	} 

	/**
	 *
	 */
	public void testDerivedOneToOneIsPickedUp(){
		departmentDomainClass = Domain.lookup(TestDomainClassReferencesDepartmentDerivedReferences.class);
		employeeDomainClass = Domain.lookup(TestDomainClassReferencesEmployee.class);
		Domain.instance().done();
				
		EReference derivedRefToEmployee = departmentDomainClass.getEReferenceNamed("mostRecentJoiner");
		assertFalse(departmentDomainClass.isMultiple(derivedRefToEmployee));
		assertTrue(departmentDomainClass.isDerived(derivedRefToEmployee));
	} 

}
