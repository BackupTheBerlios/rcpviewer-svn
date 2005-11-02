package org.essentialplatform.session;

import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.domain.Domain;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.persistence.inmemory.InMemoryObjectStore;

public class TestSessionAttachDetach extends AbstractRuntimeTestCase  {

	public TestSessionAttachDetach() {
		super(null);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCanAttachToSessionIfIdMatches() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = session.create(domainClass);
		session.detach(domainObject);
		assertEquals(session.getId(), domainObject.getSessionId());
		session.attach(domainObject);
	}

	public void testCanDetachFromSessionThroughDomainObject() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = session.create(domainClass);
		assertTrue(session.isAttached(domainObject));
		session.detach(domainObject);
		assertFalse(session.isAttached(domainObject));
	}

	public void testCannotAttachToSessionIfAlreadyAttached() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = session.create(domainClass);
		assertTrue(session.isAttached(domainObject));
		try {
			session.attach(domainObject);
			fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testDetachFromSesionNotifiesListeners() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = 
			session.create(domainClass);
		MySessionListener l = session.addSessionListener(new MySessionListener());
		session.detach(domainObject);
		assertFalse(l.attachedCallbackCalled);
		assertTrue(l.detachedCallbackCalled);
	}

	public void testCanReAttachFromSessionThroughDomainObject() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = session.create(domainClass);
		session.detach(domainObject);
		assertFalse(session.isAttached(domainObject));
		session.attach(domainObject);
		assertTrue(session.isAttached(domainObject));
	}

	public void testDomainObjectSessionIdNotRemovedWhenDetached() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = session.create(domainClass);
		String sessionIdBeforeDetach = domainObject.getSessionId();
		session.detach(domainObject);
		assertEquals(sessionIdBeforeDetach, domainObject.getSessionId());
	}

	public void testCannotDetachFromSessionIfAlreadyDetached() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = session.create(domainClass);
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
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = session.create(domainClass);
		session.detach(domainObject);
		assertEquals(session.getId(), domainObject.getSessionId());
		session.attach(domainObject);
		
		ISession session2 = sessionManager.createSession(session.getDomain(), session.getObjectStore());
		assertFalse(session.getId() == session2.getId());
		
		try {
			session2.attach(domainObject);
			fail("IllegalArgumentException should have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testCannotClearDomainObjectSessionIdIfAttached() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = session.create(domainClass);
		assertTrue(domainObject.isAttached());
		try {
			domainObject.clearSessionId();
			fail("IllegalStateException should have been thrown");
		} catch(IllegalStateException ex) {
			// expected
		}
	}


	public void testCanClearDomainObjectSessionIdIfDetached() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = session.create(domainClass);
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
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<?> domainObject = session.create(domainClass);
		assertTrue(domainObject.isAttached());
		session.detach(domainObject);
		domainObject.clearSessionId();
		
		ISession session2 = sessionManager.createSession(session.getDomain(), session.getObjectStore());
		String session2Id = session2.getId();
		
		session2.attach(domainObject);
		
		assertTrue(domainObject.isAttached());
		assertEquals(session2Id, domainObject.getSessionId());
	}
	
	public void testDomainObjectCannotBeAttachedToSessionForDifferentDomain() {
		
		// set up sessions
		ISession sessionForDefaultDomain = session;
		assertEquals("default", sessionForDefaultDomain.getDomain().getName());
		
		Domain marketingDomain = Domain.instance("marketing");
		
		ISession sessionForMarketingDomain = sessionManager.createSession(marketingDomain, new InMemoryObjectStore());
		
		// create domain object from default domain
		IDomainClass departmentDomainClass = 
			(IDomainClass)lookupAny(Department.class);
		IDomainObject<?> departmentDomainObject = 
			sessionForDefaultDomain.create(departmentDomainClass);

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
