package org.essentialplatform.progmodel.standard;
import org.essentialplatform.progmodel.standard.*;

import java.lang.reflect.Method;

import org.essentialplatform.progmodel.standard.namingconventions.Department;
import org.essentialplatform.progmodel.standard.namingconventions.Employee;


import junit.framework.TestCase;

public class TestStandardProgModelRules extends TestCase {

	private EssentialProgModelStandardSemanticsRuntimeRules namingConventions;
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
		namingConventions = new EssentialProgModelStandardSemanticsRuntimeRules();

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
		namingConventions = null;
		super.tearDown();
	}
	
	public void testIsAccessorWhenIs() {
		assertTrue(namingConventions.isAccessor(departmentGetNameMethod));
	}
	
	public void testIsAccessorWhenNot(final Method method) {
		assertFalse(namingConventions.isAccessor(departmentNotAnAccessorMethod));
	}

	public void testIsAccessorWhenIsActuallyAReference() {
		assertFalse(namingConventions.isAccessor(departmentGetSupervisorMethod));
	}

	public void testIsAccessorWhenIsActuallyACollection() {
		assertFalse(namingConventions.isAccessor(departmentGetEmployeesMethod));
	}

	public void testIsMutatorWhenIs() {
		assertTrue(namingConventions.isMutator(departmentSetNameMethod));
	}

	public void testIsMutatorWhenNot() {
		assertFalse(namingConventions.isMutator(departmentNotAMutatorMethod));
	}

	public void testIsMutatorWhenIsActuallyAReference() {
		assertFalse(namingConventions.isMutator(departmentSetSupervisorMethod));
	}

	public void testAssertAccessorWhenIs() {
		try {
			namingConventions.assertAccessor(departmentGetNameMethod);
		} catch(Exception ex) {
			fail("Assertion should succeed.");
		}
	}
	
	public void testAssertAccessorWhenNot() {
		try {
			namingConventions.assertAccessor(departmentNotAnAccessorMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void testAssertAccessorWhenActuallyAReference() {
		try {
			namingConventions.assertAccessor(departmentGetSupervisorMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void testAssertAccessorWhenActuallyACollection() {
		try {
			namingConventions.assertAccessor(departmentGetEmployeesMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void testAssertMutatorWhenIs() {
		try {
			namingConventions.assertMutator(departmentSetNameMethod);
		} catch(AssertionError error) {
			fail("Assertion should succeed.");
		}
	}

	public void testAssertMutatorWhenNot() {
		try {
			namingConventions.assertMutator(departmentNotAMutatorMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void testAssertMutatorWhenActuallyAReference() {
		try {
			namingConventions.assertMutator(departmentSetSupervisorMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void incompletetestAccessorTypeWhenValue() {
		// TODO
	}

	public void incompletetestAccessorTypeWhenSimpleReference() {
		// TODO
	}

	public void incompletetestAccessorTypeWhenCollection() {
		// TODO
	}

	public void incompletetestAccessorTypeWhenNotAnAccessor() {
		// TODO
	}

	public void incompletetestMutatorTypeWhenValue() {
		// TODO
	}
	
	public void incompletetestMutatorTypeWhenSimpleReference() {
		// TODO
	}
	
	public void incompletetestMutatorTypeWhenCollection() {
		// TODO
	}
	
	public void incompletetestMutatorTypeWhenNotAMutator() {
		// TODO
	}
	
	/**
	 * Whether supplied getter and setter are compatible, that is, that they
	 * have the same type and the same name. 
	 */
	public void incompletetestIsCompatibleWhenAre() {
		// TODO
	}
	
	public void incompletetestIsCompatibleWhenNot() {
		// TODO
	}
	
	public void incompletetestIsCompatibleWhenNotGivenAnAccessor() {
		// TODO
	}
	
	public void incompletetestIsCompatibleWhenNotGivenAMutator() {
		// TODO
	}
	
	public void testDeriveAttributeNameForAccessor() {
		assertEquals("name", namingConventions.deriveAttributeName(departmentGetNameMethod));
	}

	public void testDeriveAttributeNameForMultiPartAccessor() {
		assertEquals("numberOfEmployees", namingConventions.deriveAttributeName(departmentGetNumberOfEmployeesMethod));
	}

	public void testDeriveAttributeNameForMutator() {
		assertEquals("name", namingConventions.deriveAttributeName(departmentSetNameMethod));
	}

	public void testDeriveAttributeNameForMultiPartMutator() {
		assertEquals("numberOfEmployees", namingConventions.deriveAttributeName(departmentSetNumberOfEmployeesMethod));
	}

	public void incompletetestDeriveAttributeNameWhenNeitherAccessorNorMutator() {
		// TODO
	}

	public void incompletetestIsValueTypeWhenIs() {
		// TODO
	}
	
	public void incompletetestIsValueTypeWhenNot() {
		// TODO
	}
	
	public void incompletetestIsReferenceTypeWhenIs() {
		// TODO
	}
	
	public void incompletetestIsReferenceTypeWhenNot() {
		// TODO
	}

	public void incompletetestIsCollectionTypeWhenIs() {
		// TODO
	}
	
	public void incompletetestIsCollectionTypeWhenNot() {
		// TODO
	}
	
	public void incompletetestIsLink() {
		// TODO
	}
	
	public void incompletetestAssertLink() {
		// TODO
	}
	
	public void incompletetestIsAssociator() {
		// TODO
	}
	
	public void incompletetestIsDissociator() {
		// TODO
	}
	
	public void incompletetestAssertAssociator() {
		// TODO
	}
	
	public void incompletetestAssertDissociator() {
		// TODO
	}
	
	public void incompletetestLinkType() {
		// TODO
	}
	
	public void incompletetestDeriveLinkName() {
		// TODO
	}
	
	/**
	 * indicates whether supplied associator and dissociator are compatible,
	 * that is, that they have the same type and the same name. 
	 * @param associator
	 * @param dissociator
	 * @return
	 */
	public void incompletetestIsLinkPairCompatible(final Method associator, final Method dissociator) {
		// TODO
	}
	

	
	
}
