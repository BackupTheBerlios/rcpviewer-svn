package org.essentialplatform.runtime.shared.tests.session;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import org.essentialplatform.runtime.shared.tests.session.fixture.Department;

public class TestSession extends AbstractRuntimeClientTestCase  {

	public TestSession() {
		super(null);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	/**
	 * Can instantiate domain object/pojo from Session.
	 * 
	 * <p>
	 * The returned object will be attached to any session.
	 */
	public void testCanInstantiateDomainObjectFromSession() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		assertNotNull(domainObject);
		assertSame(domainObject.getDomainClass(), domainClass);
		Department pojo = domainObject.getPojo();
		assertNotNull(pojo);
		assertSame(Department.class, pojo.getClass());
		assertTrue(clientSession.isAttached(domainObject));
	}

	public void testDomainObjectSessionBindingTakenFromManagingSession() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		assertEquals(clientSession.getSessionBinding(), domainObject.getSessionBinding());
	}


	/**
	 * Listeners are notified when an object is created, because the objects
	 * are initially attached to the session.
	 * 
	 * <p>
	 * The returned object will be attached to any session.
	 */
	public void testSessionListenersNotifiedThatInstantiatedDomainObjectAreAttached() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		MySessionListener l = clientSession.addSessionListener(new MySessionListener());
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		assertTrue(l.attachedCallbackCalled);
		assertFalse(l.detachedCallbackCalled);
	}


	/**
	 * Can create a domain object through the session. 
	 */
	public void testDomainObjectMarkedAsPersistentIfNotTransientOnly() {
		IDomainClass domainClass = lookupAny(Department.class);

		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		assertTrue(domainObject.isPersistent());
	}


	/**
	 * TODO: need the TransientOnly annotation in the ExtendedDomainClass 
	 */
	public void incompletetestDomainObjectMarkedAsTransientIfTransientOnly() {
//		IDomainClass domainClass = lookupAny(DepartmentTransientOnly.class);
//
//		IDomainObject<DepartmentTransientOnly> domainObject = session.create(domainClass);
//		assertFalse(domainObject.isPersistent());
	}



	// marked as incomplete, needs to be refactored or removed since persistence now done through xactns.
	public void incompletetestCanRetrieveOncePersisted() {
//		IDomainClass domainClass = lookupAny(Department.class);
//
//		IDomainObject<Department> domainObject = session.create(domainClass);
//		Department dept = domainObject.getPojo();
//		dept.setName("HR"); // name is used in Department's toString() -> title
//		// domainObject.persist();
//		Department dept2 = 
//			(Department)objectStore.findByTitle(Department.class, "HR");
//		assertSame(dept2, dept);
//		IDomainObject<Department> domainObject2 = session.getDomainObjectFor(dept2, Department.class);
//		assertSame(domainObject2, domainObject);
	}

}
