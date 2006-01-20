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
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		clientSession.detach(domainObject);
		assertEquals(clientSession.getSessionBinding(), domainObject.getSessionBinding());
		clientSession.attach(domainObject);
	}

	public void testCanDetachFromSessionThroughDomainObject() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		assertTrue(clientSession.isAttached(domainObject));
		clientSession.detach(domainObject);
		assertFalse(clientSession.isAttached(domainObject));
	}

	public void testCannotAttachToSessionIfAlreadyAttached() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		assertTrue(clientSession.isAttached(domainObject));
		try {
			clientSession.attach(domainObject);
			fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testDetachFromSesionNotifiesListeners() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			clientSession.create(domainClass);
		MySessionListener l = clientSession.addSessionListener(new MySessionListener());
		clientSession.detach(domainObject);
		assertFalse(l.attachedCallbackCalled);
		assertTrue(l.detachedCallbackCalled);
	}

	public void testCanReAttachFromSessionThroughDomainObject() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		clientSession.detach(domainObject);
		assertFalse(clientSession.isAttached(domainObject));
		clientSession.attach(domainObject);
		assertTrue(clientSession.isAttached(domainObject));
	}

	public void testDomainObjectSessionBindingNotRemovedWhenDetached() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		SessionBinding sessionBindingBeforeDetach = domainObject.getSessionBinding();
		clientSession.detach(domainObject);
		assertEquals(sessionBindingBeforeDetach, domainObject.getSessionBinding());
	}

	public void testCannotDetachFromSessionIfAlreadyDetached() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		clientSession.detach(domainObject);
		assertFalse(clientSession.isAttached(domainObject));
		try {
			clientSession.detach(domainObject);
			fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testCannotAttachToSessionIfBindingDoesNotMatch() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		assertEquals(clientSession.getSessionBinding(), domainObject.getSessionBinding());
		clientSession.detach(domainObject);
		assertEquals(clientSession.getSessionBinding(), domainObject.getSessionBinding()); // unchanged
		
		IClientSession session2 = clientSessionManager.defineSession(
				new SessionBinding(clientSession.getDomain().getName(), "OBJECTSTORE#2"));
		assertFalse(clientSession.getSessionBinding().equals(session2.getSessionBinding()));
		
		try {
			session2.attach(domainObject);
			fail("IllegalArgumentException should have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testCannotClearDomainObjectSessionIdIfAttached() {
		IDomainClass domainClass = lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
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
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		assertTrue(domainObject.isAttached());
		clientSession.detach(domainObject);
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
		
		IDomainObject<Department> domainObject = clientSession.create(domainClass);
		assertTrue(domainObject.isAttached());
		clientSession.detach(domainObject);
		domainObject.clearSessionBinding();
		assertNull(domainObject.getSessionBinding());
		
		IClientSession session2 = 
			clientSessionManager.defineSession(new SessionBinding(clientSession.getDomain().getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID + "_2"));
		String session2ObjectStoreId = session2.getObjectStoreId();
		
		session2.attach(domainObject);
		
		assertTrue(domainObject.isAttached());
		assertEquals(session2.getSessionBinding(), domainObject.getSessionBinding());
	}
	
	public void testDomainObjectCannotBeAttachedToSessionForDifferentDomain() {
		
		// set up sessions
		IClientSession sessionForDefaultDomain = clientSession;
		assertEquals("default", sessionForDefaultDomain.getDomain().getName());
		
		Domain marketingDomain = Domain.instance("marketing");
		
		IClientSession sessionForMarketingDomain = clientSessionManager.defineSession(new SessionBinding(marketingDomain.getName(), "MarketingObjectStore"));
		
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
