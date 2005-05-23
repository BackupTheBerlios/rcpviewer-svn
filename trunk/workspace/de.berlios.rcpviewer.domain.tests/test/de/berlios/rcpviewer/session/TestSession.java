package de.berlios.rcpviewer.session;

import java.util.List;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.session.local.Session;
import de.berlios.rcpviewer.progmodel.standard.impl.Department;

public class TestSession extends AbstractTestCase  {

	private static class MySessionListener implements ISessionListener {
		boolean attachedCallbackCalled = false;
		boolean detachedCallbackCalled = false;
		public void domainObjectAttached(SessionObjectEvent event) {
			attachedCallbackCalled=true;
		}
		public void domainObjectDetached(SessionObjectEvent event) {
			detachedCallbackCalled=true;
		}
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
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		Domain.instance().done();
		
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
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
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
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		Domain.instance().done();
		
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
		IDomainClass<Department> domainClass = 
			domain.lookupAny(Department.class);
		Domain.instance().done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertFalse(domainObject.isPersistent());
	}


	public void testCanRetrieveOncePersisted() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
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
