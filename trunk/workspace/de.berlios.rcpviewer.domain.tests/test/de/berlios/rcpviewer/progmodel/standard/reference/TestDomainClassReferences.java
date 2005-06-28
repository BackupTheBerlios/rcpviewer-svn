package de.berlios.rcpviewer.progmodel.standard.reference;


import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.reference.Department;
import de.berlios.rcpviewer.progmodel.standard.reference.DepartmentDerivedReferences;
import de.berlios.rcpviewer.progmodel.standard.reference.DepartmentImmutableEmployeeCollection;
import de.berlios.rcpviewer.progmodel.standard.reference.Employee;
import de.berlios.rcpviewer.progmodel.standard.reference.EmployeeImmutableNameRef;
import de.berlios.rcpviewer.progmodel.standard.reference.ReferencesName;

public abstract class TestDomainClassReferences extends AbstractTestCase {

	public TestDomainClassReferences(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainAnalyzer) {
		super(domainSpecifics, domainAnalyzer);
	}

	private IDomainClass<?> departmentDomainClass;
	private IDomainClass<?> employeeDomainClass;
	private IDomainClass<ReferencesName> nameDomainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

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
		departmentDomainClass = lookupAny(Department.class);
		employeeDomainClass = lookupAny(Employee.class);
		
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
		departmentDomainClass = lookupAny(DepartmentImmutableEmployeeCollection.class);
		employeeDomainClass = lookupAny(Employee.class);
		
		assertEquals(1, departmentDomainClass.references().size());
		EReference refToEmployees = departmentDomainClass.references().get(0);
		assertEquals("employees", refToEmployees.getName());
		assertFalse(departmentDomainClass.isChangeable(refToEmployees));
	} 

	public void testImmutableOneToOneIsPickedUp(){
		employeeDomainClass = lookupAny(EmployeeImmutableNameRef.class);
		nameDomainClass = lookupAny(ReferencesName.class);
		
		assertEquals(1, employeeDomainClass.references().size());
		EReference refToName = employeeDomainClass.references().get(0);
		assertEquals("name", refToName.getName());
		assertSame(nameDomainClass, employeeDomainClass.getReferencedClass(refToName));
		assertFalse(employeeDomainClass.isChangeable(refToName));
	} 

	public void testDerivedOneToManyIsPickedUp(){
		departmentDomainClass = lookupAny(DepartmentDerivedReferences.class);
		
		EReference derivedRefToEmployees = departmentDomainClass.getEReferenceNamed("terminatedEmployees");
		assertEquals("terminatedEmployees", derivedRefToEmployees.getName());
		assertTrue(departmentDomainClass.isMultiple(derivedRefToEmployees));
		assertTrue(departmentDomainClass.isDerived(derivedRefToEmployees));
	} 

	public void testDerivedOneToOneIsPickedUp(){
		departmentDomainClass = lookupAny(DepartmentDerivedReferences.class);
		employeeDomainClass = lookupAny(Employee.class);
				
		EReference derivedRefToEmployee = departmentDomainClass.getEReferenceNamed("mostRecentJoiner");
		assertFalse(departmentDomainClass.isMultiple(derivedRefToEmployee));
		assertTrue(departmentDomainClass.isDerived(derivedRefToEmployee));
	} 
	
	/**
	 * Department 1 <-> m Employee, as annotated from Department.
	 *
	 * <p>
	 * Note that {@link de.berlios.rcpviewer.domain.IDomain#done()} must be
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
	 * Note that {@link de.berlios.rcpviewer.domain.IDomain#done()} must be
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
	 * Note that {@link de.berlios.rcpviewer.domain.IDomain#done()} must be
	 * called in order for bidirectional relationships to be identified.
	 */
	public void testBidirectionalRelationshipIsPickedUpAnnotatedFromBothSides() {
		
		departmentDomainClass = lookupAny(BiDir3Department.class);
		employeeDomainClass = lookupAny(BiDir3Employee.class);
		// getDomainInstance().done(); // necessary for bidir.

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
