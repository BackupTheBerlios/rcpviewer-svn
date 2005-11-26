package org.essentialplatform.runtime.tests.persistence;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithNoIdentifier;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIdFirst;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleStringId;
import org.essentialplatform.runtime.CompositeIdPersistenceIdAssigner;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.persistence.PersistenceId;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;

public class TestCompositeIdPersistenceIdAssigner extends AbstractRuntimeTestCase {

	private CompositeIdPersistenceIdAssigner assigner;

	private IDomainClass domainClass;
	private IDomainObject<?> dobj;
	private PersistenceId persistenceId;
	private Object[] componentValues;
	
	public TestCompositeIdPersistenceIdAssigner() {
		super(null);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
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

	public void xtestAssignedPersistenceIdHasCorrectJavaClass() {
		domainClass = lookupAny(CustomerWithSimpleStringId.class);
		assigner = new CompositeIdPersistenceIdAssigner(domainClass);
		
		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		assertEquals(CustomerWithSimpleStringId.class, persistenceId.getJavaClass());
	}

	public void xtestEachDomainObjectHasItsPersistenceIdAssigned() {
		domainClass = lookupAny(CustomerWithSimpleIdFirst.class);
		assigner = new CompositeIdPersistenceIdAssigner(domainClass);

		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		assertNotNull(dobj.getPersistenceId());
		assertSame(persistenceId, dobj.getPersistenceId());
	}

	public void xtestCompositeIfNoIdentifierFails() {
		domainClass = lookupAny(CustomerWithNoIdentifier.class);
		assigner = new CompositeIdPersistenceIdAssigner(domainClass);
		
		dobj = session.create(domainClass);

		try {
			persistenceId = assigner.assignPersistenceIdFor(dobj);
			fail("IllegalArgumentException should have been thrown.");
		} catch(IllegalArgumentException ex) {
			// expected
		} catch(Exception ex) {
			fail("IllegalArgumentException should have been thrown.");
		} 
		
	}

	public void testCompositeIfSimpleStringId() {
		domainClass = lookupAny(CustomerWithSimpleStringId.class);
		assigner = new CompositeIdPersistenceIdAssigner(domainClass);
		
		dobj = session.create(domainClass);
		((CustomerWithSimpleStringId)dobj.getPojo()).setId("foobar");

		persistenceId = assigner.assignPersistenceIdFor(dobj);

		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals("foobar", componentValues[0]);
	}

}
