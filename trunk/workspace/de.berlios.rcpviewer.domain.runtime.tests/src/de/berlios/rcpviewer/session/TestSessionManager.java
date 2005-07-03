package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;

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
		RuntimeDomain domain = RuntimeDomain.instance();
		IObjectStore objectStore = new InMemoryObjectStore();
		ISession session = sessionManager.createSession(domain, objectStore);
		assertNotNull(session.getId());
	}

	public void testEachSessionGetsADifferentId() {
		RuntimeDomain domain = RuntimeDomain.instance();
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
		RuntimeDomain domain = RuntimeDomain.instance();
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
		RuntimeDomain domain = RuntimeDomain.instance();
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
		RuntimeDomain domain = RuntimeDomain.instance();
		assertFalse(sml.sessionCreated);
		assertNull(sml.session);
		ISession session1 = sessionManager.createSession(domain, new InMemoryObjectStore());
		assertTrue(sml.sessionCreated);
		assertSame(session1, sml.session);
	}
	
	public void testSessionManagerListenerCalledWhenRemoveSession() {
		MySessionManagerListener sml = new MySessionManagerListener();
		sessionManager.addSessionManagerListener(sml);
		RuntimeDomain domain = RuntimeDomain.instance();
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
		RuntimeDomain domain = RuntimeDomain.instance();
		
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
