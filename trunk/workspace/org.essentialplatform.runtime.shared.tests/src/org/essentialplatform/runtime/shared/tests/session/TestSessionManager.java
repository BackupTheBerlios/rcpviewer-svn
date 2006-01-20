package org.essentialplatform.runtime.shared.tests.session;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.client.session.event.ISessionManagerListener;
import org.essentialplatform.runtime.client.session.event.SessionManagerEvent;
import org.essentialplatform.runtime.shared.persistence.PersistenceConstants;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;

public class TestSessionManager extends AbstractRuntimeClientTestCase  {

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
		IClientSession session = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID));
		assertNotNull(session.getObjectStoreId());
	}

	public void testEachSessionGetsADifferentId() {
		Domain domain = Domain.instance();
		IClientSession session1 = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID));
		IClientSession session2 = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_2"));
		IClientSession session3 = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_3"));
		IClientSession session4 = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_4"));
		IClientSession session5 = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_5"));
		
		assertNotSame(session1.getObjectStoreId(), session2.getObjectStoreId());
		assertNotSame(session1.getObjectStoreId(), session3.getObjectStoreId());
		assertNotSame(session1.getObjectStoreId(), session4.getObjectStoreId());
		assertNotSame(session1.getObjectStoreId(), session5.getObjectStoreId());
		assertNotSame(session2.getObjectStoreId(), session1.getObjectStoreId());
		assertNotSame(session2.getObjectStoreId(), session3.getObjectStoreId());
		assertNotSame(session2.getObjectStoreId(), session4.getObjectStoreId());
		assertNotSame(session2.getObjectStoreId(), session5.getObjectStoreId());
		assertNotSame(session3.getObjectStoreId(), session1.getObjectStoreId());
		assertNotSame(session3.getObjectStoreId(), session2.getObjectStoreId());
		assertNotSame(session3.getObjectStoreId(), session4.getObjectStoreId());
		assertNotSame(session3.getObjectStoreId(), session5.getObjectStoreId());
		assertNotSame(session4.getObjectStoreId(), session1.getObjectStoreId());
		assertNotSame(session4.getObjectStoreId(), session2.getObjectStoreId());
		assertNotSame(session4.getObjectStoreId(), session3.getObjectStoreId());
		assertNotSame(session4.getObjectStoreId(), session5.getObjectStoreId());
		assertNotSame(session5.getObjectStoreId(), session1.getObjectStoreId());
		assertNotSame(session5.getObjectStoreId(), session2.getObjectStoreId());
		assertNotSame(session5.getObjectStoreId(), session3.getObjectStoreId());
		assertNotSame(session5.getObjectStoreId(), session4.getObjectStoreId());
	}

	public void testGetAllSessions() {
		Domain domain = Domain.instance();
		IClientSession[] sessions = new IClientSession[5];
		for(int i=0; i<sessions.length; i++) {
			sessions[i] = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_"+i));
		}
		assertEquals(6, clientSessionManager.getAllSessions().size()); // 1 added in fixture
		clientSessionManager.getAllSessions().contains(clientSession); // in the fixture
		for(IClientSession session: sessions) {
			clientSessionManager.getAllSessions().contains(session);	
		}
	}

	public void testCanSwitchCurrentSession() {
		Domain domain = Domain.instance();
		IClientSession session1 = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_1"));
		assertSame(session1, clientSessionManager.getCurrentSession(domain));
		IClientSession session2 = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_2"));
		assertSame(session2, clientSessionManager.getCurrentSession(domain));
		
		clientSessionManager.switchSessionTo(domain, session1.getObjectStoreId());
		assertSame(session1, clientSessionManager.getCurrentSession(domain));
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
		clientSessionManager.addSessionManagerListener(sml);
		Domain domain = Domain.instance();
		assertFalse(sml.sessionCreated);
		assertNull(sml.session);
		IClientSession session1 = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_1"));
		assertTrue(sml.sessionCreated);
		assertSame(session1, sml.session);
	}
	
	public void testSessionManagerListenerCalledWhenRemoveSession() {
		MySessionManagerListener sml = new MySessionManagerListener();
		clientSessionManager.addSessionManagerListener(sml);
		Domain domain = Domain.instance();
		IClientSession session1 = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_1"));

		sml.session = null; // reset since creating session would have populated
		assertFalse(sml.sessionRemoved);
		assertNull(sml.session);
		clientSessionManager.removeSession(domain, session1.getObjectStoreId());
		assertTrue(sml.sessionRemoved);
		assertSame(session1, sml.session);
	}
	
	public void testSessionManagerListenerCalledWhenSwitchSession() {
		MySessionManagerListener sml = new MySessionManagerListener();
		clientSessionManager.addSessionManagerListener(sml);
		Domain domain = Domain.instance();
		
		assertFalse(sml.sessionNowCurrent);
		assertNull(sml.session);
		// create session; as a byproduct, will get a switch session
		IClientSession session1 = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_1"));
		assertTrue(sml.sessionNowCurrent);
		assertSame(session1, sml.session);

		sml.sessionNowCurrent = false;
		sml.session = null;
		assertFalse(sml.sessionNowCurrent);
		assertNull(sml.session);
		IClientSession session2 = clientSessionManager.defineSession(new SessionBinding(domain.getName(), PersistenceConstants.DEFAULT_OBJECT_STORE_ID+"_2"));
		assertTrue(sml.sessionNowCurrent);
		assertSame(session2, sml.session);
		
		sml.sessionNowCurrent = false;
		assertFalse(sml.sessionNowCurrent);
		clientSessionManager.switchSessionTo(domain, session1.getObjectStoreId());
		assertTrue(sml.sessionNowCurrent);
		assertSame(session1, sml.session);
	}
	

}
