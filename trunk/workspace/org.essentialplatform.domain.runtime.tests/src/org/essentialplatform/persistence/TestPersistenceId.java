package org.essentialplatform.persistence;

import junit.framework.TestCase;
import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.IDeploymentSpecifics;
import org.essentialplatform.RuntimeDomainSpecifics;
import org.essentialplatform.domain.IDomain;
import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.Domain;
import org.essentialplatform.persistence.inmemory.InMemoryObjectStore;
import org.essentialplatform.progmodel.standard.ProgModelConstants;
import org.essentialplatform.session.ISession;
import org.essentialplatform.session.local.SessionManager;
import org.essentialplatform.transaction.ITransactionManager;
import org.essentialplatform.transaction.internal.TransactionManager;

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