package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;

public class TestSession extends AbstractRuntimeTestCase  {

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
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = session.create(domainClass);
		assertNotNull(domainObject);
		assertSame(domainObject.getDomainClass(), domainClass);
		Department pojo = (Department)domainObject.getPojo();
		assertNotNull(pojo);
		assertSame(Department.class, pojo.getClass());
		assertTrue(session.isAttached(domainObject));
	}

	public void testDomainObjectSessionIdTakenFromManagingSession() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = session.create(domainClass);
		assertEquals(session.getId(), domainObject.getSessionId());
	}


	/**
	 * Listeners are notified when an object is created, because the objects
	 * are initially attached to the session.
	 * 
	 * <p>
	 * The returned object will be attached to any session.
	 */
	public void testSessionListenersNotifiedThatInstantiatedDomainObjectAreAttached() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		MySessionListener l = session.addSessionListener(new MySessionListener());
		IDomainObject<?> domainObject = session.create(domainClass);
		assertTrue(l.attachedCallbackCalled);
		assertFalse(l.detachedCallbackCalled);
	}


	/**
	 * Can create a domain object through the session. 
	 */
	public void testDomainObjectMarkedAsPersistentIfNotTransientOnly() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		domain.addBuilder(new EssentialProgModelExtendedSemanticsDomainBuilder());
		domain.done();

		IDomainObject<?> domainObject = session.create(domainClass);
		assertTrue(domainObject.isPersistent());
	}


	/**
	 * TODO: need the TransientOnly annotation in the ExtendedDomainClass 
	 */
	public void incompletetestDomainObjectMarkedAsTransientIfTransientOnly() {
//		IDomainClass domainClass = 
//			(IDomainClass)lookupAny(DepartmentTransientOnly.class);
//		domain.addBuilder(new ExtendedProgModelDomainBuilder());
//		domain.done();
//
//		IDomainObject<DepartmentTransientOnly> domainObject = 
//			(IDomainObject<DepartmentTransientOnly>)session.create(domainClass);
//		assertFalse(domainObject.isPersistent());
	}



	// marked as incomplete, needs to be refactored or removed since persistence now done through xactns.
	public void incompletetestCanRetrieveOncePersisted() {
//		IDomainClass domainClass = 
//			(IDomainClass)lookupAny(Department.class);
//
//		IDomainObject<Department> domainObject = 
//			(IDomainObject<Department>)session.create(domainClass);
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
