package org.essentialplatform.runtime.shared.tests.session;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.runtime.shared.persistence.PersistenceConstants;
import org.essentialplatform.runtime.shared.session.IClientSession;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.session.event.ISessionManagerListener;
import org.essentialplatform.runtime.shared.session.event.SessionManagerEvent;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeTestCase;

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
		IClientSession session = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID));
		assertNotNull(session.getId());
	}

	public void testEachSessionGetsADifferentId() {
		Domain domain = Domain.instance();
		IClientSession session1 = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID));
		IClientSession session2 = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_2"));
		IClientSession session3 = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_3"));
		IClientSession session4 = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_4"));
		IClientSession session5 = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_5"));
		
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
		IClientSession[] sessions = new IClientSession[5];
		for(int i=0; i<sessions.length; i++) {
			sessions[i] = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_"+i));
		}
		assertEquals(6, sessionManager.getAllSessions().size()); // 1 added in fixture
		sessionManager.getAllSessions().contains(session); // in the fixture
		for(IClientSession session: sessions) {
			sessionManager.getAllSessions().contains(session);	
		}
	}

	public void testCanSwitchCurrentSession() {
		Domain domain = Domain.instance();
		IClientSession session1 = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_1"));
		assertSame(session1, sessionManager.getCurrentSession(domain));
		IClientSession session2 = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_2"));
		assertSame(session2, sessionManager.getCurrentSession(domain));
		
		sessionManager.switchSessionTo(session1.getId());
		assertSame(session1, sessionManager.getCurrentSession(domain));
	}
	
	private static class MySessionManagerListener implements ISessionManagerListener {
		public boolean sessionCreated;
		public boolean sessionNowCurrent;
		public IClientSession session;
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
		IClientSession session1 = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_1"));
		assertTrue(sml.sessionCreated);
		assertSame(session1, sml.session);
	}
	
	public void testSessionManagerListenerCalledWhenRemoveSession() {
		MySessionManagerListener sml = new MySessionManagerListener();
		sessionManager.addSessionManagerListener(sml);
		Domain domain = Domain.instance();
		IClientSession session1 = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_1"));

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
		IClientSession session1 = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_1"));
		assertTrue(sml.sessionNowCurrent);
		assertSame(session1, sml.session);

		sml.sessionNowCurrent = false;
		sml.session = null;
		assertFalse(sml.sessionNowCurrent);
		assertNull(sml.session);
		IClientSession session2 = sessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_2"));
		assertTrue(sml.sessionNowCurrent);
		assertSame(session2, sml.session);
		
		sml.sessionNowCurrent = false;
		assertFalse(sml.sessionNowCurrent);
		sessionManager.switchSessionTo(session1.getId());
		assertTrue(sml.sessionNowCurrent);
		assertSame(session1, sml.session);
	}
	

}
