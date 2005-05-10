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
	 * TODO: workaround
	 * @author dkhaywood
	 *
	 */
	public static class Department implements DomainMarker {

		private String firstName;
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		private Set<Employee> employees = new HashSet<Employee>();
		@Associates(Employee.class)
		public Set<Employee> getEmployees() {
			return employees ;
		}
		void addToEmployees(final Employee employee) {
			employees.add(employee);
//			employee.setDepartment(this);
		}
		void removeFromEmployees(final Employee employee) {
			employees.remove(employee);
//			employee.setDepartment(null);
		}
	}
	public static class Employee implements DomainMarker {
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
//		private Department department;
//		public Department getDepartment() {
//			return department;
//		}
//		/**
//		 * not public since not an operation.
//		 * @param department
//		 */
//		void setDepartment(Department department) {
//			department.addToEmployees(this);
//		}
//		/**
//		 * presence to indicate an optional reference.
//		 *
//		 */
//		public void clearDepartment() {
//			department.removeFromEmployees(this);
//		}
	}
	
	private MetaModel metaModel;
	private IDomainClass<Department> departmentDomainClass;
	private IDomainClass<Employee> employeeDomainClass;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = new MetaModel();
	}

	protected void tearDown() throws Exception {
		metaModel = null;
		departmentDomainClass = null;
		employeeDomainClass = null;
		super.tearDown();
	}
	

	public void testOneToManySetIsPickedUp(){
		departmentDomainClass = metaModel.register(Department.class);
		employeeDomainClass = metaModel.register(Employee.class);
		metaModel.done();
		
		assertEquals(1, departmentDomainClass.references().size());
		EReference departmentsRef = departmentDomainClass.references().get(0);
		assertSame(employeeDomainClass, departmentDomainClass.getReferencedClass(departmentsRef));
		assertTrue(departmentDomainClass.isMultiple(departmentsRef));
		assertFalse(departmentDomainClass.isOrdered(departmentsRef));
		assertFalse(departmentDomainClass.isContainer(departmentsRef));
		assertTrue(departmentDomainClass.isUnique(departmentsRef));
		
//		assertEquals(1, employeeDomainClass.references().size());
//		EReference employeesRef = employeeDomainClass.references().get(0);
//		assertSame(departmentDomainClass, departmentDomainClass.getReferencedClass(employeesRef));
//		assertFalse(employeeDomainClass.isMultiple(employeesRef));
//		assertFalse(employeeDomainClass.isOrdered(employeesRef));
//		assertFalse(employeeDomainClass.isContainer(employeesRef));
//		assertFalse(employeeDomainClass.isUnique(employeesRef));
		
	} 
}
