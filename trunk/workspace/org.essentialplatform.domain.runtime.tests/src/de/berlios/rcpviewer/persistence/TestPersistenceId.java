package de.berlios.rcpviewer.persistence;

import junit.framework.TestCase;
import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.transaction.ITransactionManager;
import de.berlios.rcpviewer.transaction.internal.TransactionManager;

/**
 * Have copied down stuff from subclass; need to simplify.
 * 
 * @author Dan Haywood
 */
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
		assertSame(Department.class, id.getType());
	}

}
