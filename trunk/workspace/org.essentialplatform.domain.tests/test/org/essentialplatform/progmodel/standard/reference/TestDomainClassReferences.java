package org.essentialplatform.progmodel.standard.reference;


import org.eclipse.emf.ecore.EReference;
import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IReference;
import org.essentialplatform.core.domain.builders.IDomainBuilder;

public abstract class TestDomainClassReferences extends AbstractTestCase {

	private IDomainClass departmentDomainClass;
	private IDomainClass employeeDomainClass;
	private IDomainClass nameDomainClass;
	
	protected void tearDown() throws Exception {
		departmentDomainClass = null;
		employeeDomainClass = null;
		nameDomainClass = null;
		super.tearDown();
	}
	

	public void testOneToManyIsPickedUp(){
		departmentDomainClass = lookupAny(Department.class);
		employeeDomainClass = lookupAny(Employee.class);
		
		assertEquals(1, departmentDomainClass.eReferences().size());
		EReference eRefToEmployees = departmentDomainClass.eReferences().get(0);
		IReference reference = employeeDomainClass.getIReference(eRefToEmployees);
		assertEquals("employees", eRefToEmployees.getName());
		assertSame(employeeDomainClass, reference.getReferencedDomainClass());
		assertTrue(reference.isMultiple());
		assertFalse(reference.isOrdered());
		assertFalse(reference.isContainer());
		assertTrue(reference.isUnique());
		assertFalse(reference.isChangeable());
		assertFalse(reference.isDerived());
	} 

	public void testOneToOneIsPickedUp(){
		departmentDomainClass = lookupAny(Department.class);
		employeeDomainClass = lookupAny(Employee.class);
		
		assertEquals(1, employeeDomainClass.eReferences().size());
		EReference eRefToEmployees = employeeDomainClass.eReferences().get(0);
		IReference reference = employeeDomainClass.getIReference(eRefToEmployees);
		assertEquals("department", eRefToEmployees.getName());
		assertSame(departmentDomainClass, reference.getReferencedDomainClass());
		assertFalse(reference.isMultiple());
		assertFalse(reference.isOrdered());
		assertFalse(reference.isContainer());
		assertFalse(reference.isUnique());
		assertFalse(reference.isChangeable());
		assertFalse(reference.isDerived());
	} 

	public void testImmutableOneToManyIsPickedUp(){
		departmentDomainClass = lookupAny(DepartmentImmutableEmployeeCollection.class);
		employeeDomainClass = lookupAny(Employee.class);
		
		assertEquals(1, departmentDomainClass.eReferences().size());
		EReference eRefToEmployees = departmentDomainClass.eReferences().get(0);
		IReference reference = employeeDomainClass.getIReference(eRefToEmployees);
		assertEquals("employees", eRefToEmployees.getName());
		assertFalse(reference.isChangeable());
	} 

	public void testImmutableOneToOneIsPickedUp(){
		employeeDomainClass = lookupAny(EmployeeImmutableNameRef.class);
		nameDomainClass = lookupAny(ReferencesName.class);
		
		assertEquals(1, employeeDomainClass.eReferences().size());
		EReference eRefToName = employeeDomainClass.eReferences().get(0);
		IReference reference = employeeDomainClass.getIReference(eRefToName);
		assertEquals("name", eRefToName.getName());
		assertSame(nameDomainClass, reference.getReferencedDomainClass());
		assertFalse(reference.isChangeable());
	} 

	public void testDerivedOneToManyIsPickedUp(){
		departmentDomainClass = lookupAny(DepartmentDerivedReferences.class);
		
		EReference derivedRefToEmployees = departmentDomainClass.getEReferenceNamed("terminatedEmployees");
		IReference reference = departmentDomainClass.getIReference(derivedRefToEmployees);
		assertEquals("terminatedEmployees", derivedRefToEmployees.getName());
		assertTrue(reference.isMultiple());
		assertTrue(reference.isDerived());
	} 

	public void testDerivedOneToOneIsPickedUp(){
		departmentDomainClass = lookupAny(DepartmentDerivedReferences.class);
		employeeDomainClass = lookupAny(Employee.class);
				
		EReference eDerivedRefToEmployee = departmentDomainClass.getEReferenceNamed("mostRecentJoiner");
		IReference reference = employeeDomainClass.getIReference(eDerivedRefToEmployee);
		assertFalse(reference.isMultiple());
		assertTrue(reference.isDerived());
	} 
	
	/**
	 * Department 1 <-> m Employee, as annotated from Department.
	 *
	 * <p>
	 * Note that {@link org.essentialplatform.core.domain.IDomain#done()} must be
	 * called in order for bidirectional relationships to be identified.
	 */
	public void testBidirectionalRelationshipIsPickedUpAnnotatedFromParent() {
		
		departmentDomainClass = lookupAny(BiDir1Department.class);
		employeeDomainClass = lookupAny(BiDir1Employee.class);

		EReference refDepartmentToEmployees = 
			departmentDomainClass.getEReferenceNamed("employees");
		
		EReference refDepartmentToEmployeesOpposite = 
						refDepartmentToEmployees.getEOpposite();
		assertNotNull(refDepartmentToEmployeesOpposite);
		
		EReference refEmployeeToDepartment = 
			employeeDomainClass.getEReferenceNamed("department");
		assertSame(refDepartmentToEmployeesOpposite, refEmployeeToDepartment);

		
		EReference refEmployeesToDepartmentOpposite = 
			refEmployeeToDepartment.getEOpposite();
		assertNotNull(refEmployeesToDepartmentOpposite);
		
		assertSame(refEmployeesToDepartmentOpposite, refDepartmentToEmployees);
}

	/**
	 * Department 1 <-> m Employee, as annotated from Child.
	 *
	 * <p>
	 * Note that {@link org.essentialplatform.core.domain.IDomain#done()} must be
	 * called in order for bidirectional relationships to be identified.
	 */
	public void testBidirectionalRelationshipIsPickedUpAnnotatedFromChild() {
		
		departmentDomainClass = lookupAny(BiDir2Department.class);
		employeeDomainClass = lookupAny(BiDir2Employee.class);

		EReference refDepartmentToEmployees = 
			departmentDomainClass.getEReferenceNamed("employees");
		
		EReference refDepartmentToEmployeesOpposite = 
						refDepartmentToEmployees.getEOpposite();
		assertNotNull(refDepartmentToEmployeesOpposite);
		
		EReference refEmployeeToDepartment = 
			employeeDomainClass.getEReferenceNamed("department");
		assertSame(refDepartmentToEmployeesOpposite, refEmployeeToDepartment);

		
		EReference refEmployeesToDepartmentOpposite = 
			refEmployeeToDepartment.getEOpposite();
		assertNotNull(refEmployeesToDepartmentOpposite);
		
		assertSame(refEmployeesToDepartmentOpposite, refDepartmentToEmployees);
	}

	/**
	 * Department 1 <-> m Employee, as annotated from both ends.
	 * 
	 * <p>
	 * Note that {@link org.essentialplatform.core.domain.IDomain#done()} must be
	 * called in order for bidirectional relationships to be identified.
	 */
	public void testBidirectionalRelationshipIsPickedUpAnnotatedFromBothSides() {
		
		departmentDomainClass = lookupAny(BiDir3Department.class);
		employeeDomainClass = lookupAny(BiDir3Employee.class);

		EReference refDepartmentToEmployees = 
			departmentDomainClass.getEReferenceNamed("employees");
		
		EReference refDepartmentToEmployeesOpposite = 
						refDepartmentToEmployees.getEOpposite();
		assertNotNull(refDepartmentToEmployeesOpposite);
		
		EReference refEmployeeToDepartment = 
			employeeDomainClass.getEReferenceNamed("department");
		assertSame(refDepartmentToEmployeesOpposite, refEmployeeToDepartment);
		
		EReference refEmployeesToDepartmentOpposite = 
			refEmployeeToDepartment.getEOpposite();
		assertNotNull(refEmployeesToDepartmentOpposite);
		
		assertSame(refEmployeesToDepartmentOpposite, refDepartmentToEmployees);
		
	}

}
