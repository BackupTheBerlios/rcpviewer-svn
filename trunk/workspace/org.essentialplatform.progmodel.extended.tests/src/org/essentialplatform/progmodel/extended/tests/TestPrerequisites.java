package org.essentialplatform.progmodel.extended.tests;

import org.essentialplatform.progmodel.essential.app.IRequirement;
import org.essentialplatform.progmodel.essential.app.Requirement;

import junit.framework.TestCase;

public class TestPrerequisites extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateForRequirementThatIsNotMet() {
		IRequirement mustProvideQuantity = Requirement.create(false, "Must provide a quantity");
		assertFalse(mustProvideQuantity.isMet());
		assertEquals("Must provide a quantity", mustProvideQuantity.getDescription());
		assertFalse(mustProvideQuantity.isNoop());
	}

	public void testCreateForRequirementThatIsMet() {
		IRequirement mustProvideQuantity = Requirement.create(false, "Must provide a quantity");
		assertFalse(mustProvideQuantity.isMet());
		assertEquals("Must provide a quantity", mustProvideQuantity.getDescription());
		assertFalse(mustProvideQuantity.isNoop());
	}

	public void testNoop() {
		IRequirement noop = Requirement.none();
		assertTrue(noop.isMet());
		assertNull(noop.getDescription());
		assertTrue(noop.isNoop());
	}

	public void testOrBetweenNotMetAndNotMet() {
		IRequirement cannotFoo = Requirement.create(false, "Cannot foo");
		IRequirement cannotBar = Requirement.create(false, "Cannot bar");
		
		// instance method
		IRequirement result = cannotFoo.or(cannotBar);
		assertFalse(result.isMet());
		assertEquals("Cannot foo UNLESS Cannot bar", result.getDescription());
		
		// static method
		result = Requirement.or(cannotFoo, cannotBar);
		assertFalse(result.isMet());
		assertEquals("Cannot foo UNLESS Cannot bar", result.getDescription());
	}

	public void testOrBetweenNotMetAndMet() {
		IRequirement cannotFoo = Requirement.create(false, "Cannot foo");
		IRequirement cannotBar = Requirement.create(true, "Cannot bar");
		
		// instance method
		IRequirement result = cannotFoo.or(cannotBar);
		assertTrue(result.isMet());
		assertEquals("Cannot foo UNLESS Cannot bar", result.getDescription());
		
		// static method
		result = Requirement.or(cannotFoo, cannotBar);
		assertTrue(result.isMet());
		assertEquals("Cannot foo UNLESS Cannot bar", result.getDescription());

	}

	public void testOrBetweenMetAndNotMet() {
		IRequirement cannotFoo = Requirement.create(false, "Cannot foo");
		IRequirement cannotBar = Requirement.create(true, "Cannot bar");
		
		// instance method
		IRequirement result = cannotFoo.or(cannotBar);
		assertTrue(result.isMet());
		assertEquals("Cannot foo UNLESS Cannot bar", result.getDescription());
		
		// static method
		result = Requirement.or(cannotFoo, cannotBar);
		assertTrue(result.isMet());
		assertEquals("Cannot foo UNLESS Cannot bar", result.getDescription());
	}

	public void testOrBetweenMetAndMet() {
		IRequirement cannotFoo = Requirement.create(true, "Cannot foo");
		IRequirement cannotBar = Requirement.create(true, "Cannot bar");
		
		// instance method
		IRequirement result = cannotFoo.or(cannotBar);
		assertTrue(result.isMet());
		assertEquals("Cannot foo UNLESS Cannot bar", result.getDescription());
		
		// static method
		result = Requirement.or(cannotFoo, cannotBar);
		assertTrue(result.isMet());
		assertEquals("Cannot foo UNLESS Cannot bar", result.getDescription());
	}

	public void testOrBetweenMetAndNoop() {
		IRequirement cannotFoo =
			cannotFoo = Requirement.create(true, "Cannot foo");
		IRequirement noop = Requirement.none();
		
		// instance method
		IRequirement result = cannotFoo.or(noop);
		assertTrue(result.isMet());
		assertEquals("Cannot foo", result.getDescription());
	
		// static method
		result = Requirement.or(cannotFoo, noop);
		assertTrue(result.isMet());
		assertEquals("Cannot foo", result.getDescription());
	}

	public void testOrBetweenNoopAndNoop() {
		IRequirement noop = Requirement.none();
		IRequirement noop2 = Requirement.none();
		
		// instance method
		IRequirement result = noop.or(noop2);
		assertTrue(result.isMet());
		assertNull(result.getDescription());
		assertTrue(result.isNoop());
	
		// static method
		result = Requirement.or(noop, noop2);
		assertTrue(result.isMet());
		assertNull(result.getDescription());
		assertTrue(result.isNoop());
	}


	public void testAndBetweenNotMetAndNotMet() {
		IRequirement cannotFoo = Requirement.create(false, "Cannot foo");
		IRequirement cannotBar = Requirement.create(false, "Cannot bar");
		
		// instance method
		IRequirement result = cannotFoo.and(cannotBar);
		assertFalse(result.isMet());
		assertEquals("Cannot foo AND Cannot bar", result.getDescription());
		
		// static method
		result = Requirement.and(cannotFoo, cannotBar);
		assertFalse(result.isMet());
		assertEquals("Cannot foo AND Cannot bar", result.getDescription());
	}

	public void testAndBetweenNotMetAndMet() {
		IRequirement cannotFoo = Requirement.create(false, "Cannot foo");
		IRequirement cannotBar = Requirement.create(true, "Cannot bar");
		
		// instance method
		IRequirement result = cannotFoo.and(cannotBar);
		assertFalse(result.isMet());
		assertEquals("Cannot foo AND Cannot bar", result.getDescription());

		// static method
		result = Requirement.and(cannotFoo, cannotBar);
		assertFalse(result.isMet());
		assertEquals("Cannot foo AND Cannot bar", result.getDescription());
	}

	public void testAndBetweenMetAndNotMet() {
		IRequirement cannotFoo = Requirement.create(true, "Cannot foo");
		IRequirement cannotBar = Requirement.create(false, "Cannot bar");
		
		// instance method
		IRequirement result = cannotFoo.and(cannotBar);
		assertFalse(result.isMet());
		assertEquals("Cannot foo AND Cannot bar", result.getDescription());

		// static method
		result = Requirement.and(cannotFoo, cannotBar);
		assertFalse(result.isMet());
		assertEquals("Cannot foo AND Cannot bar", result.getDescription());
	}

	public void testAndBetweenMetAndMet() {
		IRequirement cannotFoo = Requirement.create(true, "Cannot foo");
		IRequirement cannotBar = Requirement.create(true, "Cannot bar");
		
		// instance method
		IRequirement result = cannotFoo.and(cannotBar);
		assertTrue(result.isMet());
		assertEquals("Cannot foo AND Cannot bar",result.getDescription());
	
		// static method
		result = Requirement.and(cannotFoo, cannotBar);
		assertTrue(result.isMet());
		assertEquals("Cannot foo AND Cannot bar",result.getDescription());
	}

	public void testAndBetweenMetAndNoop() {
		IRequirement cannotFoo = Requirement.create(true, "Cannot foo");
		IRequirement noop = Requirement.none();
		
		// instance method
		IRequirement result = cannotFoo.and(noop);
		assertTrue(result.isMet());
		assertEquals("Cannot foo", result.getDescription());
	
		// static method
		result = Requirement.and(cannotFoo, noop);
		assertTrue(result.isMet());
		assertEquals("Cannot foo", result.getDescription());
	}


	public void testRealWorldCombination() {
		IRequirement cannotFoo =
			cannotFoo = Requirement.create(false, "Cannot foo");
		IRequirement noopRequirement = Requirement.none();
		IRequirement cannotBar = Requirement.create(true, "Cannot bar");
		IRequirement cannotBaz = Requirement.create(false, "Cannot baz");
		IRequirement cannotBoz = Requirement.create(false, "Cannot boz");
		IRequirement cannotFoz = Requirement.create(true, "Cannot foz");
		
		IRequirement result = Requirement.and(
						cannotFoo, 
						Requirement.or(noopRequirement, cannotBar, cannotBaz, cannotBoz),
						cannotFoz);
		assertFalse(result.isMet());
		assertEquals("Cannot foo AND Cannot bar UNLESS Cannot baz UNLESS Cannot boz AND Cannot foz", result.getDescription());
	}
}
