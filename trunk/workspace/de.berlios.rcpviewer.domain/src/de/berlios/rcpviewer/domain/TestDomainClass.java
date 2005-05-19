package de.berlios.rcpviewer.domain;

import junit.framework.TestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
//import de.berlios.rcpviewer.progmodel.standard.impl.Department;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.Session;

public class TestDomainClass extends TestCase  {

	private Domain domain;
	private ISession session;
	protected void setUp() throws Exception {
		super.setUp();
		domain = Domain.instance();
		session = new Session();
	}

	protected void tearDown() throws Exception {
		domain.reset();
		domain = null;
		super.tearDown();
	}

	public void testCreateTransientCreatesUnderlyingPojo() {
		IDomainClass<Department> departmentDomainClass = 
			domain.lookup(Department.class);
		
		IDomainObject<Department> departmentDomainObject = 
			session.createTransient(departmentDomainClass);
		assertNotNull(departmentDomainObject.getPojo());
		assertSame(Department.class, departmentDomainObject.getPojo().getClass());
	}


	/**
	 * Can instantiate domain object/pojo directly from DomainClass.
	 * 
	 * <p>
	 * The returned object will not be attached to any session.
	 */
	public void testCanInstantiateDomainObjectFromDomainClass() {
		domain = Domain.instance();
		IDomainClass<Department> domainClass = 
			domain.lookup(Department.class);
		
		IDomainObject<Department> domainObject = domainClass.createTransient();
		assertNotNull(domainObject);
		assertSame(domainObject.getDomainClass(), domainClass);
		Department pojo = domainObject.getPojo();
		assertNotNull(pojo);
		assertSame(Department.class, pojo.getClass());
		assertFalse(session.isAttached(domainObject));
		assertFalse(session.isAttached(domainObject.getPojo()));
	}


//	/**
//	 * Cannot instantiate if don't play by the rules.
//	 */
//	public void testCannotInstantiateDomainObjectWithoutNoArgConstructor() {
//		IDomainClass<DepartmentWithoutNoArgConstructor> domainClass = 
//			domain.lookup(DepartmentWithoutNoArgConstructor.class);
//
//		try {
//			IDomainObject domainObject = domainClass.createTransient();
//			fail("Expected exception to have been thrown.");
//		} catch(ProgrammingModelException ex) {
//			// expected
//		}
//	}

	

}
