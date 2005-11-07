package org.essentialplatform.runtime.tests.progmodel.standard;
import org.essentialplatform.core.fixture.progmodel.essential.standard.namingconventions.Department;
import org.essentialplatform.core.fixture.progmodel.essential.standard.namingconventions.Employee;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeRules;
import org.essentialplatform.progmodel.standard.*;

import java.lang.reflect.Method;



import junit.framework.TestCase;

public class TestEssentialProgModelStandardRules extends TestCase {

	private EssentialProgModelRuntimeRules rules;
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
		rules = new EssentialProgModelRuntimeRules();

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
		rules = null;
		super.tearDown();
	}
	
	public void testIsAccessorWhenIs() {
		assertTrue(rules.isAccessor(departmentGetNameMethod));
	}
	
	public void testIsAccessorWhenNot(final Method method) {
		assertFalse(rules.isAccessor(departmentNotAnAccessorMethod));
	}

	public void testIsAccessorWhenIsActuallyAReference() {
		assertFalse(rules.isAccessor(departmentGetSupervisorMethod));
	}

	public void testIsAccessorWhenIsActuallyACollection() {
		assertFalse(rules.isAccessor(departmentGetEmployeesMethod));
	}

	public void testIsMutatorWhenIs() {
		assertTrue(rules.isMutator(departmentSetNameMethod));
	}

	public void testIsMutatorWhenNot() {
		assertFalse(rules.isMutator(departmentNotAMutatorMethod));
	}

	public void testIsMutatorWhenIsActuallyAReference() {
		assertFalse(rules.isMutator(departmentSetSupervisorMethod));
	}

	public void testAssertAccessorWhenIs() {
		try {
			rules.assertAccessor(departmentGetNameMethod);
		} catch(Exception ex) {
			fail("Assertion should succeed.");
		}
	}
	
	public void testAssertAccessorWhenNot() {
		try {
			rules.assertAccessor(departmentNotAnAccessorMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void testAssertAccessorWhenActuallyAReference() {
		try {
			rules.assertAccessor(departmentGetSupervisorMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void testAssertAccessorWhenActuallyACollection() {
		try {
			rules.assertAccessor(departmentGetEmployeesMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void testAssertMutatorWhenIs() {
		try {
			rules.assertMutator(departmentSetNameMethod);
		} catch(AssertionError error) {
			fail("Assertion should succeed.");
		}
	}

	public void testAssertMutatorWhenNot() {
		try {
			rules.assertMutator(departmentNotAMutatorMethod);
			fail("Assertion error expected.");
		} catch(AssertionError error) {
		}
	}

	public void testAssertMutatorWhenActuallyAReference() {
		try {
			rules.assertMutator(departmentSetSupervisorMethod);
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
		assertEquals("name", rules.deriveAttributeName(departmentGetNameMethod));
	}

	public void testDeriveAttributeNameForMultiPartAccessor() {
		assertEquals("numberOfEmployees", rules.deriveAttributeName(departmentGetNumberOfEmployeesMethod));
	}

	public void testDeriveAttributeNameForMutator() {
		assertEquals("name", rules.deriveAttributeName(departmentSetNameMethod));
	}

	public void testDeriveAttributeNameForMultiPartMutator() {
		assertEquals("numberOfEmployees", rules.deriveAttributeName(departmentSetNumberOfEmployeesMethod));
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
