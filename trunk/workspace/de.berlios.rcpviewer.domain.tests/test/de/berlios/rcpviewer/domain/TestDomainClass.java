package de.berlios.rcpviewer.domain;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
//import de.berlios.rcpviewer.progmodel.standard.impl.Department;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionFactory;
import de.berlios.rcpviewer.session.ISessionManager;
import de.berlios.rcpviewer.session.local.Session;
import de.berlios.rcpviewer.session.local.SessionFactory;
import de.berlios.rcpviewer.session.local.SessionManager;

public class TestDomainClass extends AbstractTestCase  {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}


	public void testCreateTransientCreatesUnderlyingPojo() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			session.createTransient(domainClass);
		assertNotNull(domainObject.getPojo());
		assertSame(Department.class, domainObject.getPojo().getClass());
	}


	/**
	 * Can instantiate domain object/pojo directly from DomainClass.
	 * 
	 * <p>
	 * The returned object will not be attached to any session.
	 */
	public void testCanInstantiateDomainObjectFromDomainClass() {
		IDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = domainClass.createTransient();
		assertNotNull(domainObject);
		assertSame(domainObject.getDomainClass(), domainClass);
		Department pojo = domainObject.getPojo();
		assertNotNull(pojo);
		assertSame(Department.class, pojo.getClass());
		assertFalse(session.isAttached(domainObject));
	}


	/**
	 * Cannot instantiate if don't play by the rules.
	 */
	public void testCannotInstantiateDomainObjectWithoutNoArgConstructor() {
		IDomainClass<DepartmentWithoutNoArgConstructor> domainClass = 
			Domain.lookupAny(DepartmentWithoutNoArgConstructor.class);

		try {
			IDomainObject<DepartmentWithoutNoArgConstructor> domainObject = 
				domainClass.createTransient();
			fail("Expected exception to have been thrown.");
		} catch(ProgrammingModelException ex) {
			// expected
		}
	}

	

}
