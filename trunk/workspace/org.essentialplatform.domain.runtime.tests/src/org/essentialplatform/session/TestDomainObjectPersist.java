package org.essentialplatform.session;

import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.domain.IDomainClass;

public class TestDomainObjectPersist extends AbstractRuntimeTestCase  {

	public TestDomainObjectPersist() {
		super(null);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDummy() {
	}

	
	// marked as incomplete, needs to be refactored or removed since persistence now done through xactns.
	public void incompletetestCanPersistThroughDomainObject() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		// domainObject.persist();
		assertTrue(domainObject.isPersistent());
	}

	/**
	 * Depending upon the programming model, the pojo may request that it is
	 * persisted.
	 * 
	 * <p>
	 * The means for doing this will be dependent on the programming model.
	 * In the standard programming model, we require that the Session is
	 * asked to persist. 
	 * 
	 * <p>
	 * TODO: the original design was to have an aspect pick up on the 'save'
	 * method.
	 */
	public void incompletetestCanPersistThroughPojo() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		// this test is marked incomplete and needs reworking anyway: persistence is now via xactn mgr.
		// session.persist(domainObject.getPojo());
		assertTrue(domainObject.isPersistent());
	}

	/**
	 * 
	 * TODO: is incomplete because Ted needs to distinguish persisted (created) vs saved (updated) -- dan
	 * ALSO marked as incomplete, needs to be refactored or removed since persistence now done through xactns.
	 */
	public void incompletetestCannotPersistMoreThanOnce() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		// domainObject.persist();
		try {
			// domainObject.persist();
			fail("IllegalStateException should have been thrown.");
		} catch(IllegalStateException ex) {
			// expected
		}
	}


}
