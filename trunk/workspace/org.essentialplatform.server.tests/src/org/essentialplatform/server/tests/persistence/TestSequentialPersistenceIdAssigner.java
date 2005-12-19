package org.essentialplatform.server.tests.persistence;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithNoIdentifier;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIdFirst;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIdSecond;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleStringId;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;
import org.essentialplatform.server.persistence.PersistenceId;
import org.essentialplatform.server.persistence.SequentialPersistenceIdAssigner;

public class TestSequentialPersistenceIdAssigner extends AbstractRuntimeTestCase {

	private SequentialPersistenceIdAssigner assigner;

	private IDomainClass domainClass;
	private IDomainObject<?> dobj;
	private PersistenceId persistenceId;
	private Object[] componentValues;
	
	public TestSequentialPersistenceIdAssigner() {
		super(null);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		assigner = new SequentialPersistenceIdAssigner();
	}

	@Override
	public void tearDown() throws Exception {
		assigner = null;
		domainClass = null;
		dobj = null;
		persistenceId = null;
		componentValues = null;
		super.tearDown();
	}

	public void testAssignedPersistenceIdHasCorrectJavaClass() {
		domainClass = lookupAny(CustomerWithSimpleStringId.class);
		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		assertEquals(CustomerWithSimpleStringId.class, persistenceId.getJavaClass());
	}

	public void testEachDomainObjectHasItsPersistenceIdAssigned() {
		domainClass = lookupAny(CustomerWithSimpleIdFirst.class);
		
		dobj = session.create(domainClass);
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		assertNotNull(dobj.getPersistenceId());
		assertSame(persistenceId, dobj.getPersistenceId());
	}

	public void testPersistenceIdsAreGeneratedSequentially() {
		domainClass = lookupAny(CustomerWithSimpleIdFirst.class);

		dobj = session.create(domainClass);
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		assertEquals(1, persistenceId.getComponentValues()[0]);

		dobj = session.create(domainClass);
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		assertEquals(2, persistenceId.getComponentValues()[0]);
	}


	public void testEachClassIsAssignedSequentially() {
		domainClass = lookupAny(CustomerWithSimpleIdFirst.class);

		dobj = session.create(domainClass);
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		assertEquals(1, persistenceId.getComponentValues()[0]);

		domainClass = lookupAny(CustomerWithSimpleIdSecond.class);

		dobj = session.create(domainClass);
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		assertEquals(1, persistenceId.getComponentValues()[0]);
	}

	public void testSequentialWhenNoIdentifier() {
		domainClass = lookupAny(CustomerWithNoIdentifier.class);
		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);

		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}

}
