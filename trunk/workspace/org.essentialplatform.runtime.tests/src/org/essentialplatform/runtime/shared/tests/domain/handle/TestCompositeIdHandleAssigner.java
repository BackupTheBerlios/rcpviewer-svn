package org.essentialplatform.runtime.shared.tests.domain.handle;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithCompositeId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithNoIdentifier;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIdFirst;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleStringId;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.handle.CompositeIdHandleAssigner;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;

public class TestCompositeIdHandleAssigner extends AbstractRuntimeClientTestCase {

	private CompositeIdHandleAssigner assigner;

	private IDomainClass domainClass;
	private IDomainObject<?> dobj;
	private Handle handle;
	private Object[] componentValues;
	
	public TestCompositeIdHandleAssigner() {
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
		handle = null;
		componentValues = null;
		super.tearDown();
	}

	public void testAssignedHandleHasCorrectJavaClass() {
		domainClass = lookupAny(CustomerWithSimpleStringId.class);
		assigner = new CompositeIdHandleAssigner(domainClass);
		
		dobj = clientSession.create(domainClass);

		handle = assigner.assignHandleFor(dobj);
		assertEquals(CustomerWithSimpleStringId.class, handle.getJavaClass());
	}

	public void testEachDomainObjectHasItsHandleAssigned() {
		domainClass = lookupAny(CustomerWithSimpleIdFirst.class);
		assigner = new CompositeIdHandleAssigner(domainClass);

		dobj = clientSession.create(domainClass);

		handle = assigner.assignHandleFor(dobj);
		assertNotNull(dobj.getHandle());
		assertSame(handle, dobj.getHandle());
	}

	public void testCompositeIfNoIdentifierFails() {
		domainClass = lookupAny(CustomerWithNoIdentifier.class);
		assigner = new CompositeIdHandleAssigner(domainClass);
		
		dobj = clientSession.create(domainClass);

		try {
			handle = assigner.assignHandleFor(dobj);
			fail("IllegalArgumentException should have been thrown.");
		} catch(IllegalArgumentException ex) {
			// expected
		} catch(Exception ex) {
			fail("IllegalArgumentException should have been thrown.");
		} 
		
	}

	public void testCompositeIfSimpleStringId() {
		domainClass = lookupAny(CustomerWithSimpleStringId.class);
		assigner = new CompositeIdHandleAssigner(domainClass);
		
		dobj = clientSession.create(domainClass);
		((CustomerWithSimpleStringId)dobj.getPojo()).setId("foobar");

		handle = assigner.assignHandleFor(dobj);

		componentValues = handle.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals("foobar", componentValues[0]);
	}

	public void testCompositeIfCompositeId() {
		domainClass = lookupAny(CustomerWithCompositeId.class);
		assigner = new CompositeIdHandleAssigner(domainClass);
		
		dobj = clientSession.create(domainClass);
		CustomerWithCompositeId pojo = ((CustomerWithCompositeId)dobj.getPojo());
		pojo.setFirstName("foo");
		pojo.setLastName("bar");

		handle = assigner.assignHandleFor(dobj);

		componentValues = handle.getComponentValues();
		assertEquals(2, componentValues.length);
		assertEquals("bar", componentValues[0]);
		assertEquals("foo", componentValues[1]);
	}

}
