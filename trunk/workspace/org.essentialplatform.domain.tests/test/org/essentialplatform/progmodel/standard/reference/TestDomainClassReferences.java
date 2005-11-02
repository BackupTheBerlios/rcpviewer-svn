package org.essentialplatform.progmodel.standard.reference;


import org.eclipse.emf.ecore.EReference;
import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.IDomainClass.IReference;

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
		
		assertEquals(1, departmentDomainClass.references().size());
		EReference eRefToEmployees = departmentDomainClass.references().get(0);
		IReference reference = employeeDomainClass.getReference(eRefToEmployees);
		assertEquals("employees", eRefToEmployees.getName());
		assertSame(employeeDomainClass, reference.getReferencedClass());
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
		
		assertEquals(1, employeeDomainClass.references().size());
		EReference eRefToEmployees = employeeDomainClass.references().get(0);
		IReference reference = employeeDomainClass.getReference(eRefToEmployees);
		assertEquals("department", eRefToEmployees.getName());
		assertSame(departmentDomainClass, reference.getReferencedClass());
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
		
		assertEquals(1, departmentDomainClass.references().size());
		EReference eRefToEmployees = departmentDomainClass.references().get(0);
		IReference reference = employeeDomainClass.getReference(eRefToEmployees);
		assertEquals("employees", eRefToEmployees.getName());
		assertFalse(reference.isChangeable());
	} 

	public void testImmutableOneToOneIsPickedUp(){
		employeeDomainClass = lookupAny(EmployeeImmutableNameRef.class);
		nameDomainClass = lookupAny(ReferencesName.class);
		
		assertEquals(1, employeeDomainClass.references().size());
		EReference eRefToName = employeeDomainClass.references().get(0);
		IReference reference = employeeDomainClass.getReference(eRefToName);
		assertEquals("name", eRefToName.getName());
		assertSame(nameDomainClass, reference.getReferencedClass());
		assertFalse(reference.isChangeable());
	} 

	public void testDerivedOneToManyIsPickedUp(){
		departmentDomainClass = lookupAny(DepartmentDerivedReferences.class);
		
		EReference derivedRefToEmployees = departmentDomainClass.getEReferenceNamed("terminatedEmployees");
		IReference reference = departmentDomainClass.getReference(derivedRefToEmployees);
		assertEquals("terminatedEmployees", derivedRefToEmployees.getName());
		assertTrue(reference.isMultiple());
		assertTrue(reference.isDerived());
	} 

	public void testDerivedOneToOneIsPickedUp(){
		departmentDomainClass = lookupAny(DepartmentDerivedReferences.class);
		employeeDomainClass = lookupAny(Employee.class);
				
		EReference eDerivedRefToEmployee = departmentDomainClass.getEReferenceNamed("mostRecentJoiner");
		IReference reference = employeeDomainClass.getReference(eDerivedRefToEmployee);
		assertFalse(reference.isMultiple());
		assertTrue(reference.isDerived());
	} 
	
	/**
	 * Department 1 <-> m Employee, as annotated from Department.
	 *
	 * <p>
	 * Note that {@link org.essentialplatform.domain.IDomain#done()} must be
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
	 * Note that {@link org.essentialplatform.domain.IDomain#done()} must be
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
	 * Note that {@link org.essentialplatform.domain.IDomain#done()} must be
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
