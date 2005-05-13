package de.berlios.rcpviewer.progmodel.standard;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.MetaModel;
import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;

public class TestDomainClassReferences extends AbstractTestCase {

	/**
	 * Has a 1:m bidirectional relationship with Employee.
	 * 
	 * TODO: DomainMarker is workaround
	 */
	public static class Department implements DomainMarker {
		private Set<Employee> employees = new HashSet<Employee>();
		/**
		 * Should be picked up as a 1:m reference to Employee.
		 * @return
		 */
		@Associates(Employee.class)
		public Set<Employee> getEmployees() {
			return employees ;
		}
		void addToEmployees(final Employee employee) {
			employees.add(employee);
			employee.setDepartment(this);
		}
		void removeFromEmployees(final Employee employee) {
			employees.remove(employee);
			employee.setDepartment(null);
		}
	}
	/**
	 * Has a m:1 bidirectional relationship with Employee.
	 * 
	 * <p>
	 * TODO: DomainMarker is workaround
	 */
	public static class Employee implements DomainMarker {
		public Employee(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}
		private String firstName;
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		private String lastName;
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		private Department department;
		/**
		 * Should be picked up as representing a 1:1 reference to Department.
		 * @return
		 */
		public Department getDepartment() {
			return department;
		}
		/**
		 * not public since not an operation.
		 * @param department
		 */
		void setDepartment(Department department) {
			department.addToEmployees(this);
		}
		/**
		 * presence to indicate an optional reference.
		 *
		 */
		public void clearDepartment() {
			department.removeFromEmployees(this);
		}
		public boolean isTerminated() {
			return false;
		}
	}
	/**
	 * Has a 1:m unidirectional relationship with Employee.
	 * TODO: DomainMarker is workaround
	 */
	public static class DepartmentImmutableEmployeeCollection implements DomainMarker {
		private Set<Employee> employees = new HashSet<Employee>();
		/**
		 * Should be picked up as an immutable 1:m reference to Employee.
		 * @return
		 */
		@Immutable
		@Associates(Employee.class)
		public Set<Employee> getEmployees() {
			return employees ;
		}
		void addToEmployees(final Employee employee) {
			employees.add(employee);
		}
		void removeFromEmployees(final Employee employee) {
			employees.remove(employee);
		}
	}
	/**
	 * TODO: DomainMarker is workaround
	 */
	public static class Name implements DomainMarker {
		public Name(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}
		private String firstName;
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		private String lastName;
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
	}
	/**
	 * Has a 1:1 unidirectional immutable relationship with Name.
	 * 
	 * TODO: DomainMarker is workaround
	 */
	public static class EmployeeImmutableNameRef implements DomainMarker {
		public EmployeeImmutableNameRef(String firstName, String lastName) {
			this.name = new Name(firstName, lastName);
		}
		private Name name;
		/**
		 * Should be picked up as an immutable 1:1 reference
		 * @return
		 */
		@Immutable
		public Name getName() {
			return name;
		}
	}
	/**
	 * Has a 1:m unidirectional relationship with Employee, and a separate
	 * 1:m derived unidirectional relationship with Employee.
	 * 
	 * TODO: DomainMarker is workaround
	 */
	public static class DepartmentDerivedReferences implements DomainMarker {
		private Set<Employee> employees = new HashSet<Employee>();
		/**
		 * Should be picked up as an immutable 1:m reference to Employee.
		 * @return
		 */
		@Immutable
		@Associates(Employee.class)
		public Set<Employee> getEmployees() {
			return employees ;
		}
		void addToEmployees(final Employee employee) {
			employees.add(employee);
		}
		void removeFromEmployees(final Employee employee) {
			employees.remove(employee);
		}

		/**
		 * Should be picked up as a derived 1:m reference to Employee.
		 * @return
		 */
		@Derived
		@Associates(Employee.class)
		public Set<Employee> getTerminatedEmployees() {
			Set<Employee> employeesNamed = new HashSet<Employee>();
			for(Employee e: employees) {
				if (e.isTerminated()) {
					employeesNamed.add(e);
				}
			}
			return employeesNamed;
		}
		
		/**
		 * Should be picked up as a derived 1:1 reference to Employee.
		 * @return
		 */
		@Derived
		public Employee getMostRecentJoiner() {
			throw new RuntimeException("not implemented");
		}

	}

	
	private MetaModel metaModel;
	private IDomainClass<?> departmentDomainClass;
	private IDomainClass<?> employeeDomainClass;
	private IDomainClass<Name> nameDomainClass;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = new MetaModel();
	}

	protected void tearDown() throws Exception {
		metaModel = null;
		departmentDomainClass = null;
		employeeDomainClass = null;
		nameDomainClass = null;
		super.tearDown();
	}
	

	public void testOneToManyIsPickedUp(){
		departmentDomainClass = metaModel.register(Department.class);
		employeeDomainClass = metaModel.register(Employee.class);
		metaModel.done();
		
		assertEquals(1, departmentDomainClass.references().size());
		EReference refToEmployees = departmentDomainClass.references().get(0);
		assertEquals("employees", refToEmployees.getName());
		assertSame(employeeDomainClass, departmentDomainClass.getReferencedClass(refToEmployees));
		assertTrue(departmentDomainClass.isMultiple(refToEmployees));
		assertFalse(departmentDomainClass.isOrdered(refToEmployees));
		assertFalse(departmentDomainClass.isContainer(refToEmployees));
		assertTrue(departmentDomainClass.isUnique(refToEmployees));
		assertTrue(departmentDomainClass.isChangeable(refToEmployees));
		assertFalse(departmentDomainClass.isDerived(refToEmployees));
	} 

	public void testOneToOneIsPickedUp(){
		departmentDomainClass = metaModel.register(Department.class);
		employeeDomainClass = metaModel.register(Employee.class);
		metaModel.done();
		
		assertEquals(1, employeeDomainClass.references().size());
		EReference refToDepartment = employeeDomainClass.references().get(0);
		assertEquals("department", refToDepartment.getName());
		assertSame(departmentDomainClass, employeeDomainClass.getReferencedClass(refToDepartment));
		assertFalse(employeeDomainClass.isMultiple(refToDepartment));
		assertFalse(employeeDomainClass.isOrdered(refToDepartment));
		assertFalse(employeeDomainClass.isContainer(refToDepartment));
		assertFalse(employeeDomainClass.isUnique(refToDepartment));
		assertTrue(employeeDomainClass.isChangeable(refToDepartment));
		assertFalse(employeeDomainClass.isDerived(refToDepartment));
	} 

	public void testImmutableOneToManyIsPickedUp(){
		departmentDomainClass = metaModel.register(DepartmentImmutableEmployeeCollection.class);
		employeeDomainClass = metaModel.register(Employee.class);
		metaModel.done();
		assertEquals(1, departmentDomainClass.references().size());
		EReference refToEmployees = departmentDomainClass.references().get(0);
		assertEquals("employees", refToEmployees.getName());
		assertFalse(departmentDomainClass.isChangeable(refToEmployees));
	} 

	public void testImmutableOneToOneIsPickedUp(){
		employeeDomainClass = metaModel.register(EmployeeImmutableNameRef.class);
		nameDomainClass = metaModel.register(Name.class);
		metaModel.done();
		assertEquals(1, employeeDomainClass.references().size());
		EReference refToName = employeeDomainClass.references().get(0);
		assertEquals("name", refToName.getName());
		assertSame(nameDomainClass, employeeDomainClass.getReferencedClass(refToName));
		assertFalse(employeeDomainClass.isChangeable(refToName));
	} 

	public void testDerivedOneToManyIsPickedUp(){
		departmentDomainClass = metaModel.register(DepartmentDerivedReferences.class);
		employeeDomainClass = metaModel.register(Employee.class);
		metaModel.done();
		
		EReference derivedRefToEmployees = departmentDomainClass.getEReferenceNamed("terminatedEmployees");
		assertEquals("terminatedEmployees", derivedRefToEmployees.getName());
		assertTrue(departmentDomainClass.isMultiple(derivedRefToEmployees));
		assertTrue(departmentDomainClass.isDerived(derivedRefToEmployees));
	} 

	/**
	 *
	 */
	public void testDerivedOneToOneIsPickedUp(){
		departmentDomainClass = metaModel.register(DepartmentDerivedReferences.class);
		employeeDomainClass = metaModel.register(Employee.class);
		metaModel.done();
		
		EReference derivedRefToEmployee = departmentDomainClass.getEReferenceNamed("mostRecentJoiner");
		assertFalse(departmentDomainClass.isMultiple(derivedRefToEmployee));
		assertTrue(departmentDomainClass.isDerived(derivedRefToEmployee));
	} 

}
