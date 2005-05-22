package de.berlios.rcpviewer.domain;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
//import de.berlios.rcpviewer.progmodel.standard.impl.Department;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.Session;

public class TestDomainClass extends AbstractTestCase  {

	private Domain domain;
	private ISession session;
	protected void setUp() throws Exception {
		super.setUp();
		session = new Session();
	}

	protected void tearDown() throws Exception {
		domain = null;
		session = null;
		Domain.reset();
		super.tearDown();
	}


	public void testCreateTransientCreatesUnderlyingPojo() {
		IDomainClass<Department> domainClass = 
			Domain.lookup(Department.class);
		domain = domainClass.getDomain();
		
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
			Domain.lookup(Department.class);
		domain = domainClass.getDomain();
		
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
			Domain.lookup(DepartmentWithoutNoArgConstructor.class);
		domain = domainClass.getDomain();

		try {
			IDomainObject<DepartmentWithoutNoArgConstructor> domainObject = 
				domainClass.createTransient();
			fail("Expected exception to have been thrown.");
		} catch(ProgrammingModelException ex) {
			// expected
		}
	}

	

}
