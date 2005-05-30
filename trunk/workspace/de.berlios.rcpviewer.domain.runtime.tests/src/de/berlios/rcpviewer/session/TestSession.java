package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;

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
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertNotNull(domainObject);
		assertSame(domainObject.getDomainClass(), domainClass);
		Department pojo = domainObject.getPojo();
		assertNotNull(pojo);
		assertSame(Department.class, pojo.getClass());
		assertTrue(session.isAttached(domainObject));
	}

	public void testDomainObjectSessionIdTakenFromManagingSession() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
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
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		
		MySessionListener l = session.addSessionListener(new MySessionListener());
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertTrue(l.attachedCallbackCalled);
		assertFalse(l.detachedCallbackCalled);
	}


	/**
	 * Can create a domain object through the session, but it will not be
	 * persistent. 
	 */
	public void testDomainObjectInitiallyTransient() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertFalse(domainObject.isPersistent());
	}


	public void testCanRetrieveOncePersisted() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);

		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		Department dept = domainObject.getPojo();
		dept.setName("HR"); // name is used in Department's toString() -> title
		domainObject.persist();
		Department dept2 = 
			(Department)objectStore.findByTitle(Department.class, "HR");
		assertSame(dept2, dept);
		IDomainObject<Department> domainObject2 = session.getDomainObjectFor(dept2, Department.class);
		assertSame(domainObject2, domainObject);
	}

}
