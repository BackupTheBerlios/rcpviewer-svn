package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;

public class TestSessionFactory extends AbstractRuntimeTestCase  {

	public TestSessionFactory() {
		super(null);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreatingSessionInheritsDomain() {
		ISession session = sessionFactory.createSession();
		assertSame(session.getDomain(), sessionFactory.getDomain());
	}

	public void testCreatingSessionInheritsObjectStore() {
		ISession session = sessionFactory.createSession();
		assertSame(session.getObjectStore(), sessionFactory.getObjectStore());
	}

	public void testCreatingSessionAllocatesId() {
		ISession session = sessionFactory.createSession();
		assertNotNull(session.getId());
	}

	public void testEachSessionGetsADifferentId() {
		ISession session1 = sessionFactory.createSession();
		ISession session2 = sessionFactory.createSession();
		ISession session3 = sessionFactory.createSession();
		ISession session4 = sessionFactory.createSession();
		ISession session5 = sessionFactory.createSession();
		
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
	
	public void testCreatingSessionCanOverrideObjectStore() {
		IObjectStore os = new InMemoryObjectStore();
		assertNotSame(os, sessionFactory.getObjectStore());
		ISession session = sessionFactory.createSession(os);
		assertSame(os, session.getObjectStore());
	}


}
