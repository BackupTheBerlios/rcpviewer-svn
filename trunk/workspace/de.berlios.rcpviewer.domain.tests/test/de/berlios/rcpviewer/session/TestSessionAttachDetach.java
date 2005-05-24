package de.berlios.rcpviewer.session;

import java.util.List;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.session.local.Session;
import de.berlios.rcpviewer.session.local.SessionFactory;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.session.Department;

public class TestSessionAttachDetach extends AbstractTestCase  {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCanAttachToSessionIfIdMatches() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		session.detach(domainObject);
		assertEquals(session.getId(), domainObject.getSessionId());
		session.attach(domainObject);
	}

	public void testCanDetachFromSessionThroughDomainObject() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		Domain.instance().done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertTrue(session.isAttached(domainObject));
		session.detach(domainObject);
		assertFalse(session.isAttached(domainObject));
	}

	public void testCannotAttachToSessionIfAlreadyAttached() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertTrue(session.isAttached(domainObject));
		try {
			session.attach(domainObject);
			fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testDetachFromSesionNotifiesListeners() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		Domain.instance().done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		MySessionListener l = session.addSessionListener(new MySessionListener());
		session.detach(domainObject);
		assertFalse(l.attachedCallbackCalled);
		assertTrue(l.detachedCallbackCalled);
	}

	public void testCanReAttachFromSessionThroughDomainObject() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		Domain.instance().done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		session.detach(domainObject);
		assertFalse(session.isAttached(domainObject));
		session.attach(domainObject);
		assertTrue(session.isAttached(domainObject));
	}

	public void testDomainObjectSessionIdNotRemovedWhenDetached() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		String sessionIdBeforeDetach = domainObject.getSessionId();
		session.detach(domainObject);
		assertEquals(sessionIdBeforeDetach, domainObject.getSessionId());
	}

	public void testCannotDetachFromSessionIfAlreadyDetached() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		session.detach(domainObject);
		assertFalse(session.isAttached(domainObject));
		try {
			session.detach(domainObject);
			fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testCannotAttachToSessionIfIdDoesNotMatch() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		session.detach(domainObject);
		assertEquals(session.getId(), domainObject.getSessionId());
		session.attach(domainObject);
		
		ISession session2 = sessionFactory.createSession();
		assertFalse(session.getId() == session2.getId());
		
		try {
			session2.attach(domainObject);
			fail("IllegalArgumentException should have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testCannotClearDomainObjectSessionIdIfAttached() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertTrue(domainObject.isAttached());
		try {
			domainObject.clearSessionId();
			fail("IllegalStateException should have been thrown");
		} catch(IllegalStateException ex) {
			// expected
		}
	}


	public void testCanClearDomainObjectSessionIdIfDetached() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertTrue(domainObject.isAttached());
		session.detach(domainObject);
		domainObject.clearSessionId();
		assertNull(domainObject.getSessionId());
	}
	
	/**
	 * Look up Department (in the "default" domain); clear its session Id, then
	 * attach to another session referring to the default domain.
	 *
	 */
	public void testDomainObjectCanBeAttachedToSessionForSameDomainIfHasNoSessionId() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertTrue(domainObject.isAttached());
		session.detach(domainObject);
		domainObject.clearSessionId();
		
		ISession session2 = sessionFactory.createSession();
		String session2Id = session2.getId();
		
		session2.attach(domainObject);
		
		assertTrue(domainObject.isAttached());
		assertEquals(session2Id, domainObject.getSessionId());
	}
	
	public void testDomainObjectCannotBeAttachedToSessionForDifferentDomain() {
		
		// set up sessions
		ISession sessionForDefaultDomain = session;
		assertEquals("default", sessionForDefaultDomain.getDomain().getName());
		
		SessionFactory sessionFactoryForMarketingDomain = new SessionFactory();
		sessionFactoryForMarketingDomain.setDomainName("marketing");
		sessionFactoryForMarketingDomain.setObjectStore(new InMemoryObjectStore());
		sessionFactoryForMarketingDomain.setSessionManager(SessionManager.instance());
		
		ISession sessionForMarketingDomain = sessionFactoryForMarketingDomain.createSession();
		
		// create domain object from default domain
		IDomainClass<Department> departmentDomainClass = 
			Domain.lookupAny(Department.class);
		IDomainObject<Department> departmentDomainObject = 
			(IDomainObject<Department>)sessionForDefaultDomain.createTransient(departmentDomainClass);

		// detach and clear its session Id
		sessionForDefaultDomain.detach(departmentDomainObject);
		departmentDomainObject.clearSessionId();

		try {
			sessionForMarketingDomain.attach(departmentDomainObject);
			fail("Expected IllegalArgumentException to have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	
	}
	
}
