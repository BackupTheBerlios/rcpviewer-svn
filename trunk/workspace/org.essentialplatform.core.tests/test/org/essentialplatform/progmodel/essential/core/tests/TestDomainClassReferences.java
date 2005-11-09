package org.essentialplatform.progmodel.essential.core.tests;


import org.eclipse.emf.ecore.EReference;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IReference;
import org.essentialplatform.core.fixture.progmodel.essential.standard.reference.BiDir1Department;
import org.essentialplatform.core.fixture.progmodel.essential.standard.reference.BiDir1Employee;
import org.essentialplatform.core.fixture.progmodel.essential.standard.reference.BiDir2Department;
import org.essentialplatform.core.fixture.progmodel.essential.standard.reference.BiDir2Employee;
import org.essentialplatform.core.fixture.progmodel.essential.standard.reference.BiDir3Department;
import org.essentialplatform.core.fixture.progmodel.essential.standard.reference.BiDir3Employee;
import org.essentialplatform.core.fixture.progmodel.essential.standard.reference.Department;
import org.essentialplatform.core.fixture.progmodel.essential.standard.reference.DepartmentDerivedReferences;
import org.essentialplatform.core.fixture.progmodel.essential.standard.reference.DepartmentImmutableEmployeeCollection;
import org.essentialplatform.core.fixture.progmodel.essential.standard.reference.Employee;
import org.essentialplatform.core.fixture.progmodel.essential.standard.reference.EmployeeImmutableNameRef;
import org.essentialplatform.core.fixture.progmodel.essential.standard.reference.ReferencesName;
import org.essentialplatform.core.tests.AbstractTestCase;

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
		
		assertEquals(1, departmentDomainClass.iReferences().size());
		IDomainClass.IReference reference = departmentDomainClass.iReferences().get(0);
		assertEquals("employees", reference.getName());
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
		
		assertEquals(1, employeeDomainClass.iReferences().size());
		IReference reference = employeeDomainClass.iReferences().get(0);
		assertEquals("department", reference.getName());
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
		
		assertEquals(1, departmentDomainClass.iReferences().size());
		IReference reference = departmentDomainClass.iReferences().get(0);
		assertEquals("employees", reference.getName());
		assertFalse(reference.isChangeable());
	} 

	public void testImmutableOneToOneIsPickedUp(){
		employeeDomainClass = lookupAny(EmployeeImmutableNameRef.class);
		nameDomainClass = lookupAny(ReferencesName.class);
		
		assertEquals(1, employeeDomainClass.iReferences().size());
		IReference reference = employeeDomainClass.iReferences().get(0);
		assertEquals("name", reference.getName());
		assertSame(nameDomainClass, reference.getReferencedDomainClass());
		assertFalse(reference.isChangeable());
	} 

	public void testDerivedOneToManyIsPickedUp(){
		departmentDomainClass = lookupAny(DepartmentDerivedReferences.class);
		
		IDomainClass.IReference reference = departmentDomainClass.getIReferenceNamed("terminatedEmployees");
		assertEquals("terminatedEmployees", reference.getName());
		assertTrue(reference.isMultiple());
		assertTrue(reference.isDerived());
	} 

	public void testDerivedOneToOneIsPickedUp(){
		departmentDomainClass = lookupAny(DepartmentDerivedReferences.class);
		employeeDomainClass = lookupAny(Employee.class);
				
		IDomainClass.IReference reference = departmentDomainClass.getIReferenceNamed("mostRecentJoiner");
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

		IDomainClass.IReference refDepartmentToEmployees = 
			departmentDomainClass.getIReferenceNamed("employees");
		
		EReference eRefDepartmentToEmployeesOpposite = 
						refDepartmentToEmployees.getEReference().getEOpposite();
		assertNotNull(eRefDepartmentToEmployeesOpposite);
		IDomainClass.IReference refDepartmentToEmployeesOpposite = 
			employeeDomainClass.getIReference(eRefDepartmentToEmployeesOpposite); 
		
		IDomainClass.IReference refEmployeeToDepartment = 
			employeeDomainClass.getIReferenceNamed("department");
		assertSame(refDepartmentToEmployeesOpposite, refEmployeeToDepartment);

		
		EReference eRefEmployeesToDepartmentOpposite = 
			refEmployeeToDepartment.getEReference().getEOpposite();
		assertNotNull(eRefEmployeesToDepartmentOpposite);
		IDomainClass.IReference refEmployeesToDepartmentOpposite = 
			departmentDomainClass.getIReference(eRefEmployeesToDepartmentOpposite);
		
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

		IReference iRefDepartmentToEmployees = 
			departmentDomainClass.getIReferenceNamed("employees");
		EReference eRefDepartmentToEmployees = 
			iRefDepartmentToEmployees.getEReference();
		
		EReference eRefDepartmentToEmployeesOpposite = 
						eRefDepartmentToEmployees.getEOpposite();
		assertNotNull(eRefDepartmentToEmployeesOpposite);
		IReference iRefDepartmentToEmployeesOpposite = 
			employeeDomainClass.getIReference(eRefDepartmentToEmployeesOpposite);
		
		IReference iRefEmployeeToDepartment = 
			employeeDomainClass.getIReferenceNamed("department");
		assertSame(iRefDepartmentToEmployeesOpposite, iRefEmployeeToDepartment);

		EReference eRefEmployeesToDepartmentOpposite = 
			iRefEmployeeToDepartment.getEReference().getEOpposite();
		assertNotNull(eRefEmployeesToDepartmentOpposite);
		IReference iRefEmployeesToDepartmentOpposite = 
			departmentDomainClass.getIReference(eRefEmployeesToDepartmentOpposite);
		
		assertSame(iRefEmployeesToDepartmentOpposite, iRefDepartmentToEmployees);
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

		IReference iRefDepartmentToEmployees = 
			departmentDomainClass.getIReferenceNamed("employees");
		EReference eRefDepartmentToEmployees = iRefDepartmentToEmployees.getEReference(); 
		
		EReference eRefDepartmentToEmployeesOpposite = 
						eRefDepartmentToEmployees.getEOpposite();
		assertNotNull(eRefDepartmentToEmployeesOpposite);
		IReference iRefDepartmentToEmployeesOpposite = 
			employeeDomainClass.getIReference(eRefDepartmentToEmployeesOpposite);
		
		IReference iRefEmployeeToDepartment = 
			employeeDomainClass.getIReferenceNamed("department");
		assertSame(iRefDepartmentToEmployeesOpposite, iRefEmployeeToDepartment);
		
		EReference eRefEmployeeToDepartment = 
			iRefEmployeeToDepartment.getEReference();
		EReference eRefEmployeesToDepartmentOpposite = 
			eRefEmployeeToDepartment.getEOpposite();
		assertNotNull(eRefEmployeesToDepartmentOpposite);
		IReference iRefEmployeesToDepartmentOpposite = 
			departmentDomainClass.getIReference(eRefEmployeesToDepartmentOpposite);
		
		assertSame(iRefEmployeesToDepartmentOpposite, iRefDepartmentToEmployees);
		
	}

}
