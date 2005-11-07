package org.essentialplatform.runtime.tests.session;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.fixture.session.Department;
import org.essentialplatform.runtime.persistence.inmemory.InMemoryObjectStore;
import org.essentialplatform.runtime.session.ISession;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;

public class TestSessionAttachDetach extends AbstractRuntimeTestCase  {

	public void testCanAttachToSessionIfIdMatches() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		session.detach(domainObject);
		assertEquals(session.getId(), domainObject.getSessionId());
		session.attach(domainObject);
	}

	public void testCanDetachFromSessionThroughDomainObject() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		assertTrue(session.isAttached(domainObject));
		session.detach(domainObject);
		assertFalse(session.isAttached(domainObject));
	}

	public void testCannotAttachToSessionIfAlreadyAttached() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		assertTrue(session.isAttached(domainObject));
		try {
			session.attach(domainObject);
			fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testDetachFromSesionNotifiesListeners() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			session.create(domainClass);
		MySessionListener l = session.addSessionListener(new MySessionListener());
		session.detach(domainObject);
		assertFalse(l.attachedCallbackCalled);
		assertTrue(l.detachedCallbackCalled);
	}

	public void testCanReAttachFromSessionThroughDomainObject() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		session.detach(domainObject);
		assertFalse(session.isAttached(domainObject));
		session.attach(domainObject);
		assertTrue(session.isAttached(domainObject));
	}

	public void testDomainObjectSessionIdNotRemovedWhenDetached() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		String sessionIdBeforeDetach = domainObject.getSessionId();
		session.detach(domainObject);
		assertEquals(sessionIdBeforeDetach, domainObject.getSessionId());
	}

	public void testCannotDetachFromSessionIfAlreadyDetached() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
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
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
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
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
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
		
		IDomainObject<Department> domainObject = session.create(domainClass);
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
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
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
		IDomainClass departmentDomainClass = lookupAny(Department.class);
		IDomainObject<Department> departmentDomainObject = 
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
