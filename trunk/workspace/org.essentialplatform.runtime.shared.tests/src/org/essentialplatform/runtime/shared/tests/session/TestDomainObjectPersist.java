package org.essentialplatform.runtime.shared.tests.session;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import org.essentialplatform.runtime.shared.tests.session.fixture.Department;

public class TestDomainObjectPersist extends AbstractRuntimeClientTestCase  {

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
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
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
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
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
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		// domainObject.persist();
		try {
			// domainObject.persist();
			fail("IllegalStateException should have been thrown.");
		} catch(IllegalStateException ex) {
			// expected
		}
	}


}
