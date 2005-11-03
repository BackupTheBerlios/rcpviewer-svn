package org.essentialplatform.runtime.tests.session;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.persistence.inmemory.InMemoryObjectStore;
import org.essentialplatform.runtime.session.ISession;
import org.essentialplatform.runtime.session.ISessionManagerListener;
import org.essentialplatform.runtime.session.SessionManagerEvent;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;

public class TestSessionManager extends AbstractRuntimeTestCase  {

	public TestSessionManager() {
		super(null);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreatingSessionAllocatesId() {
		Domain domain = Domain.instance();
		IObjectStore objectStore = new InMemoryObjectStore();
		ISession session = sessionManager.createSession(domain, objectStore);
		assertNotNull(session.getId());
	}

	public void testEachSessionGetsADifferentId() {
		Domain domain = Domain.instance();
		ISession session1 = sessionManager.createSession(domain, new InMemoryObjectStore());
		ISession session2 = sessionManager.createSession(domain, new InMemoryObjectStore());
		ISession session3 = sessionManager.createSession(domain, new InMemoryObjectStore());
		ISession session4 = sessionManager.createSession(domain, new InMemoryObjectStore());
		ISession session5 = sessionManager.createSession(domain, new InMemoryObjectStore());
		
		assertNotSame(session1.getId(), session2.getId());
		assertNotSame(session1.getId(), session3.getId());
		assertNotSame(session1.getId(), session4.getId());
		assertNotSame(session1.getId(), session5.getId());
		assertNotSame(session2.getId(), session1.getId());
		assertNotSame(session2.getId(), session3.getId());
		assertNotSame(session2.getId(), session4.getId());
		assertNotSame(session2.getId(), session5.getId());
		assertNotSame(session3.getId(), session1.getId());
		assertNotSame(session3.getId(), session2.getId());
		assertNotSame(session3.getId(), session4.getId());
		assertNotSame(session3.getId(), session5.getId());
		assertNotSame(session4.getId(), session1.getId());
		assertNotSame(session4.getId(), session2.getId());
		assertNotSame(session4.getId(), session3.getId());
		assertNotSame(session4.getId(), session5.getId());
		assertNotSame(session5.getId(), session1.getId());
		assertNotSame(session5.getId(), session2.getId());
		assertNotSame(session5.getId(), session3.getId());
		assertNotSame(session5.getId(), session4.getId());
	}

	public void testGetAllSessions() {
		Domain domain = Domain.instance();
		ISession[] sessions = new ISession[5];
		for(int i=0; i<sessions.length; i++) {
			sessions[i] = sessionManager.createSession(domain, new InMemoryObjectStore());
		}
		assertEquals(6, sessionManager.getAllSessions().size()); // 1 added in fixture
		sessionManager.getAllSessions().contains(session); // in the fixture
		for(ISession session: sessions) {
			sessionManager.getAllSessions().contains(session);	
		}
	}

	public void testCanSwitchCurrentSession() {
		Domain domain = Domain.instance();
		ISession session1 = sessionManager.createSession(domain, new InMemoryObjectStore());
		assertEquals(session1.getId(), sessionManager.getCurrentSessionId());
		ISession session2 = sessionManager.createSession(domain, new InMemoryObjectStore());
		assertEquals(session2.getId(), sessionManager.getCurrentSessionId());
		
		sessionManager.switchSessionTo(session1.getId());
		assertEquals(session1.getId(), sessionManager.getCurrentSessionId());
	}
	
	private static class MySessionManagerListener implements ISessionManagerListener {
		public boolean sessionCreated;
		public boolean sessionNowCurrent;
		public ISession session;
		public boolean sessionRemoved;
		public void sessionCreated(SessionManagerEvent event) {
			sessionCreated = true;
			session = event.getSession();
		}

		public void sessionRemoved(SessionManagerEvent event) {
			sessionRemoved = true;
			session = event.getSession();
		}

		public void sessionNowCurrent(SessionManagerEvent event) {
			sessionNowCurrent = true;
			session = event.getSession();
		}
	}

	public void testSessionManagerListenerCalledWhenCreateSession() {
		MySessionManagerListener sml = new MySessionManagerListener();
		sessionManager.addSessionManagerListener(sml);
		Domain domain = Domain.instance();
		assertFalse(sml.sessionCreated);
		assertNull(sml.session);
		ISession session1 = sessionManager.createSession(domain, new InMemoryObjectStore());
		assertTrue(sml.sessionCreated);
		assertSame(session1, sml.session);
	}
	
	public void testSessionManagerListenerCalledWhenRemoveSession() {
		MySessionManagerListener sml = new MySessionManagerListener();
		sessionManager.addSessionManagerListener(sml);
		Domain domain = Domain.instance();
		ISession session1 = sessionManager.createSession(domain, new InMemoryObjectStore());

		sml.session = null; // reset since creating session would have populated
		assertFalse(sml.sessionRemoved);
		assertNull(sml.session);
		sessionManager.removeSession(session1.getId());
		assertTrue(sml.sessionRemoved);
		assertSame(session1, sml.session);
	}
	
	public void testSessionManagerListenerCalledWhenSwitchSession() {
		MySessionManagerListener sml = new MySessionManagerListener();
		sessionManager.addSessionManagerListener(sml);
		Domain domain = Domain.instance();
		
		assertFalse(sml.sessionNowCurrent);
		assertNull(sml.session);
		// create session; as a byproduct, will get a switch session
		ISession session1 = sessionManager.createSession(domain, new InMemoryObjectStore());
		assertTrue(sml.sessionNowCurrent);
		assertSame(session1, sml.session);

		sml.sessionNowCurrent = false;
		sml.session = null;
		assertFalse(sml.sessionNowCurrent);
		assertNull(sml.session);
		ISession session2 = sessionManager.createSession(domain, new InMemoryObjectStore());
		assertTrue(sml.sessionNowCurrent);
		assertSame(session2, sml.session);
		
		sml.sessionNowCurrent = false;
		assertFalse(sml.sessionNowCurrent);
		sessionManager.switchSessionTo(session1.getId());
		assertTrue(sml.sessionNowCurrent);
		assertSame(session1, sml.session);
	}
	

}
