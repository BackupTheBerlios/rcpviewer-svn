package de.berlios.rcpviewer.session;

import java.util.List;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.session.local.Session;
import de.berlios.rcpviewer.progmodel.standard.impl.Department;

public class TestSessionFactory extends AbstractTestCase  {

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

}
