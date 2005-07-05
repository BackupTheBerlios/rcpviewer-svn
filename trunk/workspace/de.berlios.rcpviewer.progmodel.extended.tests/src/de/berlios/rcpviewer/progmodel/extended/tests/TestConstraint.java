package de.berlios.rcpviewer.progmodel.extended.tests;

import de.berlios.rcpviewer.progmodel.extended.Constraint;
import de.berlios.rcpviewer.progmodel.extended.IConstraint;
import junit.framework.TestCase;

public class TestConstraint extends TestCase {

	private IConstraint constraint;
	private IConstraint constraint2;
	private IConstraint result;
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateForConstraintThatDoesInitiallyApply() {
		constraint = Constraint.create(true, "can't edit the quantity");
		assertTrue(constraint.applies());
		assertEquals("can't edit the quantity", constraint.getMessage());
	}

	public void testCreateForConstraintThatDoesNotInitiallyApply() {
		constraint = Constraint.create(false, "edit away!");
		assertFalse(constraint.applies());
		assertEquals(null, constraint.getMessage());
	}

	public void testNoop() {
		constraint = Constraint.noop();
		assertFalse(constraint.applies());
		assertNull(constraint.getMessage());
	}

	public void testIsNoopWhenIs() {
		constraint = Constraint.noop();
		assertTrue(constraint.isNoop());
	}

	public void testIsNoopWhenInFactDoesNotApply() {
		constraint = Constraint.create(false, "Whatever");
		assertFalse(constraint.isNoop());
	}

	public void testIsNoopWhenInFactDoesApply() {
		constraint = Constraint.create(true, "Whatever");
		assertFalse(constraint.isNoop());
	}

	public void testOrBetweenNotApplyAndNotApply() {
		constraint = Constraint.create(false, "Cannot foo");
		constraint2 = Constraint.create(false, "Cannot bar");
		
		// instance method
		result = constraint.or(constraint2, "Cannot foo and also bar");
		assertFalse(result.applies());
		assertEquals(null, result.getMessage());
		
		// static method
		result = Constraint.or("Cannot foo and also bar", constraint, constraint2);
		assertFalse(result.applies());
		assertEquals(null, result.getMessage());
	}

	public void testOrBetweenNotApplyAndApply() {
		constraint = Constraint.create(false, "Cannot foo");
		constraint2 = Constraint.create(true, "Cannot bar");
		
		// instance method
		IConstraint result = constraint.or(constraint2, "Cannot foo and also bar");
		assertTrue(result.applies());
		assertEquals("Cannot bar", result.getMessage()); // message from one that applies
		
		// static method
		result = Constraint.or("Cannot foo and also bar", constraint, constraint2);
		assertTrue(result.applies());
		assertEquals("Cannot bar", result.getMessage());

	}

	public void testOrBetweenApplyAndNotApply() {
		constraint = Constraint.create(true, "Cannot foo");
		constraint2 = Constraint.create(false, "Cannot bar");
		
		// instance method
		IConstraint result = constraint.or(constraint2, "Cannot foo and also bar");
		assertTrue(result.applies());
		assertEquals("Cannot foo", result.getMessage()); // message from one that applies

		// static method
		result = Constraint.or("Cannot foo and also bar", constraint, constraint2);
		assertTrue(result.applies());
		assertEquals("Cannot foo", result.getMessage());
	}

	public void testOrBetweenApplyAndApply() {
		constraint = Constraint.create(true, "Cannot foo");
		constraint2 = Constraint.create(true, "Cannot bar");
		
		// instance method
		IConstraint result = constraint.or(constraint2, "Cannot foo and also bar");
		assertTrue(result.applies());
		assertEquals("Cannot foo and also bar", result.getMessage()); // use message supplied

		// static method
		result = Constraint.or("Cannot foo and also bar", constraint, constraint2);
		assertTrue(result.applies());
		assertEquals("Cannot foo and also bar", result.getMessage()); // use message supplied
	}


	public void testAndBetweenNotApplyAndNotApply() {
		constraint = Constraint.create(false, "Cannot foo");
		constraint2 = Constraint.create(false, "Cannot bar");
		
		// instance method
		result = constraint.and(constraint2);
		assertFalse(result.applies());
		assertEquals(null, result.getMessage());
		
		// static method
		result = Constraint.and(constraint, constraint2);
		assertFalse(result.applies());
		assertEquals(null, result.getMessage());
	}

	public void testAndBetweenNotApplyAndApply() {
		constraint = Constraint.create(false, "Cannot foo");
		constraint2 = Constraint.create(true, "Cannot bar");
		
		// instance method
		IConstraint result = constraint.and(constraint2);
		assertFalse(result.applies());
		assertEquals(null, result.getMessage()); // no message

		// static method
		result = Constraint.and(constraint, constraint2);
		assertFalse(result.applies());
		assertEquals(null, result.getMessage());
	}

	public void testAndBetweenApplyAndNotApply() {
		constraint = Constraint.create(true, "Cannot foo");
		constraint2 = Constraint.create(false, "Cannot bar");
		
		// instance method
		IConstraint result = constraint.and(constraint2);
		assertFalse(result.applies());
		assertEquals(null, result.getMessage()); // no message
	
		// static method
		result = Constraint.and(constraint, constraint2);
		assertFalse(result.applies());
		assertEquals(null, result.getMessage()); // no message
	}

	public void testAndBetweenApplyAndApply() {
		constraint = Constraint.create(true, "Cannot foo");
		constraint2 = Constraint.create(true, "Cannot bar");
		
		// instance method
		IConstraint result = constraint.and(constraint2);
		assertTrue(result.applies());
		assertEquals("Cannot foo; Cannot bar", result.getMessage()); // message is constructed automatically
	
		// static method
		result = Constraint.and(constraint, constraint2);
		assertTrue(result.applies());
		assertEquals("Cannot foo; Cannot bar", result.getMessage()); // message is constructed automatically
	}

}
