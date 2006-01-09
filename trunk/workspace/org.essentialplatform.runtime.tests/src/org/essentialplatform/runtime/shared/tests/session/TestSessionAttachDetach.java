package org.essentialplatform.runtime.shared.tests.session;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.PersistenceConstants;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import org.essentialplatform.runtime.shared.tests.session.fixture.Department;

public class TestSessionAttachDetach extends AbstractRuntimeClientTestCase  {

	public void testCanAttachToSessionIfBindingMatches() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		session.detach(domainObject);
		assertEquals(session.getSessionBinding(), domainObject.getSessionBinding());
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

	public void testDomainObjectSessionBindingNotRemovedWhenDetached() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		SessionBinding sessionBindingBeforeDetach = domainObject.getSessionBinding();
		session.detach(domainObject);
		assertEquals(sessionBindingBeforeDetach, domainObject.getSessionBinding());
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

	public void testCannotAttachToSessionIfBindingDoesNotMatch() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		assertEquals(session.getSessionBinding(), domainObject.getSessionBinding());
		session.detach(domainObject);
		assertEquals(session.getSessionBinding(), domainObject.getSessionBinding()); // unchanged
		
		IClientSession session2 = sessionManager.defineSession(
				new SessionBinding(session.getDomain().getName(), "OBJECTSTORE#2"));
		assertFalse(session.getSessionBinding().equals(session2.getSessionBinding()));
		
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
			domainObject.clearSessionBinding();
			fail("IllegalStateException should have been thrown");
		} catch(IllegalStateException ex) {
			// expected
		}
	}


	public void testCanClearDomainObjectBindingIfDetached() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		assertTrue(domainObject.isAttached());
		session.detach(domainObject);
		domainObject.clearSessionBinding();
		assertNull(domainObject.getSessionBinding());
	}
	
	/**
	 * Look up Department (in the "default" domain); clear its session binding,
	 * then attach to another session referring to the default domain.
	 *
	 */
	public void testDomainObjectCanBeAttachedToSessionForSameDomainIfHasNoSessionBinding() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = session.create(domainClass);
		assertTrue(domainObject.isAttached());
		session.detach(domainObject);
		domainObject.clearSessionBinding();
		assertNull(domainObject.getSessionBinding());
		
		IClientSession session2 = 
			sessionManager.defineSession(new SessionBinding(session.getDomain().getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID + "_2"));
		String session2ObjectStoreId = session2.getObjectStoreId();
		
		session2.attach(domainObject);
		
		assertTrue(domainObject.isAttached());
		assertEquals(session2.getSessionBinding(), domainObject.getSessionBinding());
	}
	
	public void testDomainObjectCannotBeAttachedToSessionForDifferentDomain() {
		
		// set up sessions
		IClientSession sessionForDefaultDomain = session;
		assertEquals("default", sessionForDefaultDomain.getDomain().getName());
		
		Domain marketingDomain = Domain.instance("marketing");
		
		IClientSession sessionForMarketingDomain = sessionManager.defineSession(new SessionBinding(marketingDomain.getName(), "MarketingObjectStore"));
		
		// create domain object from default domain
		IDomainClass departmentDomainClass = lookupAny(Department.class);
		IDomainObject<Department> departmentDomainObject = 
			sessionForDefaultDomain.create(departmentDomainClass);

		// detach and clear its session Id
		sessionForDefaultDomain.detach(departmentDomainObject);
		departmentDomainObject.clearSessionBinding();

		try {
			sessionForMarketingDomain.attach(departmentDomainObject);
			fail("Expected IllegalArgumentException to have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	
	}
	
}
