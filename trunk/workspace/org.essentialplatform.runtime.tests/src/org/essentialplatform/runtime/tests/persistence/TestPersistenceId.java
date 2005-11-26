package org.essentialplatform.runtime.tests.persistence;

import org.essentialplatform.runtime.fixture.persistence.Department;
import org.essentialplatform.runtime.fixture.persistence.Employee;
import org.essentialplatform.runtime.persistence.PersistenceId;

import junit.framework.TestCase;

public class TestPersistenceId extends TestCase {

	private PersistenceId id1;
	private PersistenceId id2;

	public TestPersistenceId() {
		super(null);
	}
	
	@Override
	public void tearDown() {
		id1 = null;
		id2 = null;
	}

	public void testWhenEqualForSameTypeSingleComponent() {
		id1 = new PersistenceId(Department.class);
		id2 = new PersistenceId(Department.class);
		
		id1.addComponentValue(new Integer(1));
		id2.addComponentValue(new Integer(1));
		
		assertTrue(id1.equals(id2));
	}

	public void testWhenNotEqualForSameTypeSingleComponent() {
		id1 = new PersistenceId(Department.class);
		id2 = new PersistenceId(Department.class);

		id1.addComponentValue(new Integer(1));
		id2.addComponentValue(new Integer(10));
		
		assertFalse(id1.equals(id2));
	}

	public void testWhenEqualForSameTypeMultipleComponents() {
		id1 = new PersistenceId(Department.class);
		id2 = new PersistenceId(Department.class);

		id1.addComponentValue(new Integer(1));
		id1.addComponentValue(new Integer(2));
		id2.addComponentValue(new Integer(1));
		id2.addComponentValue(new Integer(2));

		assertTrue(id1.equals(id2));
	}

	public void testWhenNotEqualForSameTypeMultipleComponents() {
		id1 = new PersistenceId(Department.class);
		id2 = new PersistenceId(Department.class);

		id1.addComponentValue(new Integer(1));
		id1.addComponentValue(new Integer(2));
		id2.addComponentValue(new Integer(1));
		id2.addComponentValue(new Integer(1));

		assertFalse(id1.equals(id2));
	}

	public void testWhenNotEqualForDifferentType() {
		id1 = new PersistenceId(Department.class);
		id2 = new PersistenceId(Employee.class);
		
		assertFalse(id1.equals(id2));
	}

	public void testGetType() {
		PersistenceId id = new PersistenceId(Department.class);
		assertSame(Department.class, id.getJavaClass());
	}

}
