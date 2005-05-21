package de.berlios.rcpviewer.progmodel.standard;

import java.lang.reflect.Method;


import junit.framework.TestCase;

public class TestNamingConventions extends TestCase {

	private NamingConventions namingConventions;
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
		namingConventions = new NamingConventions();

		departmentGetNameMethod = TestNamingConventionsDepartment.class.getMethod("getName", new Class[]{});
		departmentNotAnAccessorMethod = TestNamingConventionsDepartment.class.getMethod("foo", new Class[]{});;
		departmentGetSupervisorMethod = TestNamingConventionsDepartment.class.getMethod("getSupervisor", new Class[]{});;
		departmentGetEmployeesMethod = TestNamingConventionsDepartment.class.getMethod("getEmployees", new Class[]{});;
		departmentSetNameMethod = TestNamingConventionsDepartment.class.getMethod("setName", new Class[]{String.class});;
		departmentNotAMutatorMethod = TestNamingConventionsDepartment.class.getMethod("bar", new Class[]{});;
		departmentSetSupervisorMethod = TestNamingConventionsDepartment.class.getMethod("setSupervisor", new Class[]{TestNamingConventionsEmployee.class});;
		departmentGetNumberOfEmployeesMethod = TestNamingConventionsDepartment.class.getMethod("getNumberOfEmployees", new Class[]{});;
		departmentSetNumberOfEmployeesMethod = TestNamingConventionsDepartment.class.getMethod("setNumberOfEmployees", new Class[]{int.class});;
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