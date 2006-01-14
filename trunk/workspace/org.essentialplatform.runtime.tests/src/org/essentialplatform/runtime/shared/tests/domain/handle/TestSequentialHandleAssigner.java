package org.essentialplatform.runtime.shared.tests.domain.handle;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithNoIdentifier;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIdFirst;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIdSecond;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleStringId;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.handle.SequentialHandleAssigner;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;

public class TestSequentialHandleAssigner extends AbstractRuntimeClientTestCase {

	private SequentialHandleAssigner assigner;

	private IDomainClass domainClass;
	private IDomainObject<?> dobj;
	private Handle persistenceId;
	private Object[] componentValues;
	
	public TestSequentialHandleAssigner() {
		super(null);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		assigner = new SequentialHandleAssigner();
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

	public void testAssignedHandleHasCorrectJavaClass() {
		domainClass = lookupAny(CustomerWithSimpleStringId.class);
		dobj = session.create(domainClass);

		persistenceId = assigner.assignHandleFor(dobj);
		assertEquals(CustomerWithSimpleStringId.class, persistenceId.getJavaClass());
	}

	public void testEachDomainObjectHasItsHandleAssigned() {
		domainClass = lookupAny(CustomerWithSimpleIdFirst.class);
		
		dobj = session.create(domainClass);
		persistenceId = assigner.assignHandleFor(dobj);
		assertNotNull(dobj.getHandle());
		assertSame(persistenceId, dobj.getHandle());
	}

	public void testHandlesAreGeneratedSequentially() {
		domainClass = lookupAny(CustomerWithSimpleIdFirst.class);

		dobj = session.create(domainClass);
		persistenceId = assigner.assignHandleFor(dobj);
		assertEquals(1, persistenceId.getComponentValues()[0]);

		dobj = session.create(domainClass);
		persistenceId = assigner.assignHandleFor(dobj);
		assertEquals(2, persistenceId.getComponentValues()[0]);
	}


	public void testEachClassIsAssignedSequentially() {
		domainClass = lookupAny(CustomerWithSimpleIdFirst.class);

		dobj = session.create(domainClass);
		persistenceId = assigner.assignHandleFor(dobj);
		assertEquals(1, persistenceId.getComponentValues()[0]);

		domainClass = lookupAny(CustomerWithSimpleIdSecond.class);

		dobj = session.create(domainClass);
		persistenceId = assigner.assignHandleFor(dobj);
		assertEquals(1, persistenceId.getComponentValues()[0]);
	}

	public void testSequentialWhenNoIdentifier() {
		domainClass = lookupAny(CustomerWithNoIdentifier.class);
		dobj = session.create(domainClass);

		persistenceId = assigner.assignHandleFor(dobj);

		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}

}
