package de.berlios.rcpviewer.metamodel;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

import de.berlios.rcpviewer.progmodel.IProgrammingModel;
import de.berlios.rcpviewer.progmodel.standard.ProgrammingModel;

import junit.framework.TestCase;

public class TestProgrammingModel extends TestCase {

	public static class Department {
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void foo() { }
		public void bar() { }
		
		private Employee supervisor;
		public Employee getSupervisor() {
			return supervisor;
		}
		public void setSupervisor(Employee supervisor) {}
		
		private List employees = new ArrayList();
		public List getEmployees() {
			return employees;
		}
		
		private int numberOfEmployees;
		public int getNumberOfEmployees() {
			return numberOfEmployees;
		}
		public void setNumberOfEmployees(int numberOfEmployees) {
			this.numberOfEmployees = numberOfEmployees;
		}
		
	}
	
	public static class Employee {}
	
	
	private IProgrammingModel programmingModel;
	private Method departmentGetNameMethod;
	private Method departmentNotAnAccessorMethod;
	private Method departmentGetSupervisorMethod;
	private Method departmentGetEmployeesMethod;
	private Method departmentSetNameMethod;
	private Method departmentNotAMutatorMethod;
	private Method departmentSetSupervisorMethod;
	private Method departmentGetNumberOfEmployeesMethod;
	private Method departmentSetNumberOfEmployeesMethod;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		programmingModel = new ProgrammingModel();

		departmentGetNameMethod = Department.class.getMethod("getName", new Class[]{});
		departmentNotAnAccessorMethod = Department.class.getMethod("foo", new Class[]{});;
		departmentGetSupervisorMethod = Department.class.getMethod("getSupervisor", new Class[]{});;
		departmentGetEmployeesMethod = Department.class.getMethod("getEmployees", new Class[]{});;
		departmentSetNameMethod = Department.class.getMethod("setName", new Class[]{String.class});;
		departmentNotAMutatorMethod = Department.class.getMethod("bar", new Class[]{});;
		departmentSetSupervisorMethod = Department.class.getMethod("setSupervisor", new Class[]{Employee.class});;
		departmentGetNumberOfEmployeesMethod = Department.class.getMethod("getNumberOfEmployees", new Class[]{});;
		departmentSetNumberOfEmployeesMethod = Department.class.getMethod("setNumberOfEmployees", new Class[]{int.class});;
	}
	
	@Override
	protected void tearDown() throws Exception {
		programmingModel = null;
		super.tearDown();
	}
	
	public void testIsAccessorWhenIs() {
		assertTrue(programmingModel.isAccessor(departmentGetNameMethod));
	}
	
	public void testIsAccessorWhenNot(final Method method) {
		assertFalse(programmingModel.isAccessor(departmentNotAnAccessorMethod));
	}

	public void testIsAccessorWhenIsActuallyAReference() {
		assertFalse(programmingModel.isAccessor(departmentGetSupervisorMethod));
	}

	public void testIsAccessorWhenIsActuallyACollection() {
		assertFalse(programmingModel.isAccessor(departmentGetEmployeesMethod));
	}

	public void testIsMutatorWhenIs() {
		assertTrue(programmingModel.isMutator(departmentSetNameMethod));
	}

	public void testIsMutatorWhenNot() {
		assertFalse(programmingModel.isMutator(departmentNotAMutatorMethod));
	}

	public void testIsMutatorWhenIsActuallyAReference() {
		assertFalse(programmingModel.isMutator(departmentSetSupervisorMethod));
	}

	public void testAssertAccessorWhenIs() {
		try {
			programmingModel.assertAccessor(departmentGetNameMethod);
		} catch(Exception ex) {
			fail("Assertion should succeed.");
		}
	}
	
	public void testAssertAccessorWhenNot() {
		try {
			programmingModel.assertAccessor(departmentNotAnAccessorMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void testAssertAccessorWhenActuallyAReference() {
		try {
			programmingModel.assertAccessor(departmentGetSupervisorMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void testAssertAccessorWhenActuallyACollection() {
		try {
			programmingModel.assertAccessor(departmentGetEmployeesMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void testAssertMutatorWhenIs() {
		try {
			programmingModel.assertMutator(departmentSetNameMethod);
		} catch(AssertionError error) {
			fail("Assertion should succeed.");
		}
	}

	public void testAssertMutatorWhenNot() {
		try {
			programmingModel.assertMutator(departmentNotAMutatorMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void testAssertMutatorWhenActuallyAReference() {
		try {
			programmingModel.assertMutator(departmentSetSupervisorMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void xtestAccessorTypeWhenValue() {
		// TODO
	}

	public void xtestAccessorTypeWhenSimpleReference() {
		// TODO
	}

	public void xtestAccessorTypeWhenCollection() {
		// TODO
	}

	public void xtestAccessorTypeWhenNotAnAccessor() {
		// TODO
	}

	public void xtestMutatorTypeWhenValue() {
		// TODO
	}
	
	public void xtestMutatorTypeWhenSimpleReference() {
		// TODO
	}
	
	public void xtestMutatorTypeWhenCollection() {
		// TODO
	}
	
	public void xtestMutatorTypeWhenNotAMutator() {
		// TODO
	}
	
	/**
	 * Whether supplied getter and setter are compatible, that is, that they
	 * have the same type and the same name. 
	 */
	public void xtestIsCompatibleWhenAre() {
		// TODO
	}
	
	public void xtestIsCompatibleWhenNot() {
		// TODO
	}
	
	public void xtestIsCompatibleWhenNotGivenAnAccessor() {
		// TODO
	}
	
	public void xtestIsCompatibleWhenNotGivenAMutator() {
		// TODO
	}
	
	public void testDeriveAttributeNameForAccessor() {
		assertEquals("name", programmingModel.deriveAttributeName(departmentGetNameMethod));
	}

	public void testDeriveAttributeNameForMultiPartAccessor() {
		assertEquals("numberOfEmployees", programmingModel.deriveAttributeName(departmentGetNumberOfEmployeesMethod));
	}

	public void testDeriveAttributeNameForMutator() {
		assertEquals("name", programmingModel.deriveAttributeName(departmentSetNameMethod));
	}

	public void testDeriveAttributeNameForMultiPartMutator() {
		assertEquals("numberOfEmployees", programmingModel.deriveAttributeName(departmentSetNumberOfEmployeesMethod));
	}

	public void xtestDeriveAttributeNameWhenNeitherAccessorNorMutator() {
		// TODO
	}

	public void xtestIsValueTypeWhenIs() {
		// TODO
	}
	
	public void xtestIsValueTypeWhenNot() {
		// TODO
	}
	
	public void xtestIsReferenceTypeWhenIs() {
		// TODO
	}
	
	public void xtestIsReferenceTypeWhenNot() {
		// TODO
	}

	public void xtestIsCollectionTypeWhenIs() {
		// TODO
	}
	
	public void xtestIsCollectionTypeWhenNot() {
		// TODO
	}
	
	public void xtestIsLink() {
		// TODO
	}
	
	public void xtestAssertLink() {
		// TODO
	}
	
	public void xtestIsAssociator() {
		// TODO
	}
	
	public void xtestIsDissociator() {
		// TODO
	}
	
	public void xtestAssertAssociator() {
		// TODO
	}
	
	public void xtestAssertDissociator() {
		// TODO
	}
	
	public void xtestLinkType() {
		// TODO
	}
	
	public void xtestDeriveLinkName() {
		// TODO
	}
	
	/**
	 * indicates whether supplied associator and dissociator are compatible,
	 * that is, that they have the same type and the same name. 
	 * @param associator
	 * @param dissociator
	 * @return
	 */
	public void xtestIsLinkPairCompatible(final Method associator, final Method dissociator) {
		// TODO
	}
	

	
	
}
