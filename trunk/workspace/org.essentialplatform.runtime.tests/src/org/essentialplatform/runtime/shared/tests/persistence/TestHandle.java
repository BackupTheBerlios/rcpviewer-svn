package org.essentialplatform.runtime.shared.tests.persistence;

import junit.framework.TestCase;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.tests.persistence.fixture.Department;
import org.essentialplatform.runtime.shared.tests.persistence.fixture.Employee;

public class TestHandle extends TestCase {

	private Handle id1;
	private Handle id2;

	public TestHandle() {
		super(null);
	}
	
	@Override
	public void tearDown() {
		id1 = null;
		id2 = null;
	}

	public void testWhenEqualForSameTypeSingleComponent() {
		id1 = new Handle(Department.class);
		id2 = new Handle(Department.class);
		
		id1.addComponentValue(new Integer(1));
		id2.addComponentValue(new Integer(1));
		
		assertTrue(id1.equals(id2));
	}

	public void testWhenNotEqualForSameTypeSingleComponent() {
		id1 = new Handle(Department.class);
		id2 = new Handle(Department.class);

		id1.addComponentValue(new Integer(1));
		id2.addComponentValue(new Integer(10));
		
		assertFalse(id1.equals(id2));
	}

	public void testWhenEqualForSameTypeMultipleComponents() {
		id1 = new Handle(Department.class);
		id2 = new Handle(Department.class);

		id1.addComponentValue(new Integer(1));
		id1.addComponentValue(new Integer(2));
		id2.addComponentValue(new Integer(1));
		id2.addComponentValue(new Integer(2));

		assertTrue(id1.equals(id2));
	}

	public void testWhenNotEqualForSameTypeMultipleComponents() {
		id1 = new Handle(Department.class);
		id2 = new Handle(Department.class);

		id1.addComponentValue(new Integer(1));
		id1.addComponentValue(new Integer(2));
		id2.addComponentValue(new Integer(1));
		id2.addComponentValue(new Integer(1));

		assertFalse(id1.equals(id2));
	}

	public void testWhenNotEqualForDifferentType() {
		id1 = new Handle(Department.class);
		id2 = new Handle(Employee.class);
		
		assertFalse(id1.equals(id2));
	}

	public void testEqualWhenCreateUsingVarArgsVsExplicitAdding() {
		id1 = new Handle(Department.class, 1);
		id2 = new Handle(Department.class);
		id2.addComponentValue(new Integer(1));
		
		assertTrue(id1.equals(id2));
	}

	public void testNotEqualWhenCreateUsingVarArgsVsExplicitAddingFalsePositive() {
		id1 = new Handle(Department.class, 1);
		id2 = new Handle(Department.class);
		id2.addComponentValue(new Integer(2)); // different
		
		assertFalse(id1.equals(id2));
	}

	public void testGetType() {
		Handle id = new Handle(Department.class);
		assertSame(Department.class, id.getJavaClass());
	}


	public void testUpdateWhenNoValuesDoesNothing() {
		id1 = new Handle(Department.class);
		id1.update(1);
		assertNull(id1.getPrevious());
		assertFalse(id1.hasPrevious());
		
		id1 = new Handle(Department.class);
		id1.update(1, 2);
		assertNull(id1.getPrevious());
		assertFalse(id1.hasPrevious());
	}
	
	public void testUpdateWhenSameValuesDoesNothing() {
		id1 = new Handle(Department.class, 1);
		id1.update(1);
		assertNull(id1.getPrevious());
		assertFalse(id1.hasPrevious());

		id1 = new Handle(Department.class, 1, 2);
		id1.update(1, 2);
		assertNull(id1.getPrevious());
		assertFalse(id1.hasPrevious());
	}


	public void testUpdateWhenNewValuesMovesCurrentToOriginal() {
		id1 = new Handle(Department.class, 1);
		id1.update();
		assertNotNull(id1.getPrevious());
		assertTrue(id1.hasPrevious());
	}

	public void testAfterUpdateTheOriginalIsEqualToTheOriginalValue() {
		id1 = new Handle(Department.class, 1);
		id2 = new Handle(Department.class, 1);  // acting as our control
		assertTrue(id1.equals(id2));
		id1.update(123);
		assertFalse(id1.equals(id2));
		assertTrue(id2.equals(id1.getPrevious()));
	}


	public void testAfterUpdateCanContinueToAddNewComponentsWhenUpdateWithNone() {
		id1 = new Handle(Department.class, 1, 2);
		id1.update();
		
		id1.addComponentValues(456, 789);
		id1.addComponentValues(123);
		
		id2 = new Handle(Department.class, 456, 789, 123);
		assertTrue(id1.equals(id2));
	}

	public void testAfterUpdateCanContinueToAddNewComponentsWhenUpdateWithSome() {
		id1 = new Handle(Department.class, 1, 2);
		id1.update(456);
		
		id1.addComponentValues(789, 123);
		
		id2 = new Handle(Department.class, 456, 789, 123);
		assertTrue(id1.equals(id2));
	}

	public void testUpdateAndThenAnotherUpdateLosesThePreviousOfTheFirst() {
		id1 = new Handle(Department.class, 1);
		id1.update(2);
		Handle id1sFirstPrevious = id1.getPrevious();
		
		id2 = new Handle(Department.class, 1);
		assertEquals(id2, id1sFirstPrevious);
		
		id1.update(3);
		Handle id1sSecondPrevious = id1.getPrevious();

		id2 = new Handle(Department.class, 2);
		assertEquals(id2, id1sSecondPrevious);
		
		// the first previous didn't get shuffle along
		assertNull(id1sFirstPrevious.getPrevious());
	}

	public void testPreviousNotUsedInEqualityCheck() {
		id1 = new Handle(Department.class, 123);
		id2 = new Handle(Department.class, 456);  
		assertFalse(id1.equals(id2));
		
		id1.update(789, 123);
		id2.update(789, 123);
		
		assertFalse(id1.getPrevious().equals(id2.getPrevious()));
		assertTrue(id1.equals(id2));
	}


	public void testGetPreviousBeforeUpdateReturnsNothing() {
		id1 = new Handle(Department.class, 1);
		assertNull(id1.getPrevious());
	}

	public void testHasPreviousBeforeUpdateReturnsFalse() {
		id1 = new Handle(Department.class, 1);
		assertFalse(id1.hasPrevious());
	}

	public void testUpdateDoesNotChangeType() {
		id1 = new Handle(Department.class, 1);
		Class<?> id1ClassBeforeUpdate = id1.getJavaClass();
		id1.update(2);
		assertSame(id1ClassBeforeUpdate, id1.getJavaClass());
	}

	public void testEqualsWithDifferentNumbersOfComponents() {
		id1 = new Handle(Department.class, 1);
		id2 = new Handle(Department.class, 1, 2);
		
		assertFalse(id1.equals(id2));
		assertFalse(id2.equals(id1));
	}


	public void testEqualsWithOneHandleWithNoComponents() {
		id1 = new Handle(Department.class);
		id2 = new Handle(Department.class, 1);
		
		assertFalse(id1.equals(id2));
		assertFalse(id2.equals(id1));
	}
	
	public void testEqualsWithNullWithNoComponents() {
		id1 = new Handle(Department.class);
		assertFalse(id1.equals(null));
	}

	public void testEqualsWithNullWithComponents() {
		id1 = new Handle(Department.class, 1, 2, 3);
		assertFalse(id1.equals(null));
	}

}
