package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IDomainClass;

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
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.create(domainClass);
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
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.create(domainClass);
		// this test is marked incomplete and needs reworking anyway: persistence is now via xactn mgr.
		// session.persist(domainObject.getPojo());
		assertTrue(domainObject.isPersistent());
	}

	// COMMENTED OUT BECAUSE (A) INCOMPLETE AND (B) THIS FUNCTIONALITY IS BEING
	// RELOCATED TO SESSION (OR AT LEAST, MOVED OUT OF RUNTIMEDOMAINCLASS)
//	/**
//	 * Create directly from DomainClass rather than from Session. 
//	 */
//	// marked as incomplete, needs to be refactored or removed since persistence now done through xactns.
//	public void incompletetestCannotPersistIfNotAttachedToSession() {
//		IDomainClass domainClass = 
//			(IDomainClass)lookupAny(Department.class);
//		
//		IDomainObject<Department> domainObject = domainClass.create(session);
//		session.detach(domainObject);
//		assertFalse(session.isAttached(domainObject));
//		try {
//			// domainObject.persist();
//			fail("IllegalStateException should have been thrown.");
//		} catch(IllegalStateException ex) {
//			// expected
//		}
//	}


	/**
	 * 
	 * TODO: is incomplete because Ted needs to distinguish persisted (created) vs saved (updated) -- dan
	 * ALSO marked as incomplete, needs to be refactored or removed since persistence now done through xactns.
	 */
	public void incompletetestCannotPersistMoreThanOnce() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.create(domainClass);
		// domainObject.persist();
		try {
			// domainObject.persist();
			fail("IllegalStateException should have been thrown.");
		} catch(IllegalStateException ex) {
			// expected
		}
	}


}
