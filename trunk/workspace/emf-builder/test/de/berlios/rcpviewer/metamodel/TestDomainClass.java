package de.berlios.rcpviewer.metamodel;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
//import de.berlios.rcpviewer.progmodel.standard.impl.Department;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.Session;

public class TestDomainClass extends AbstractTestCase  {

	private MetaModel metaModel;
	private ISession session;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = new MetaModel();
		session = new Session();
	}

	protected void tearDown() throws Exception {
		metaModel = null;
		super.tearDown();
	}

	public void testCreateTransientCreatesUnderlyingPojo() {
		IDomainClass<Department> departmentDomainClass = 
			metaModel.lookup(Department.class);
		
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
		metaModel = MetaModel.threadInstance();
		IDomainClass<Department> domainClass = 
			metaModel.lookup(Department.class);
		
		IDomainObject<Department> domainObject = domainClass.createTransient();
		assertNotNull(domainObject);
		assertSame(domainObject.getDomainClass(), domainClass);
		Department pojo = domainObject.getPojo();
		assertNotNull(pojo);
		assertSame(Department.class, pojo.getClass());
		assertFalse(session.isAttached(domainObject));
		assertFalse(session.isAttached(domainObject.getPojo()));
	}


	/**
	 * Cannot instantiate if don't play by the rules.
	 */
	public void testCannotInstantiateDomainObjectWithoutNoArgConstructor() {
		IDomainClass<DepartmentWithoutNoArgConstructor> domainClass = 
			metaModel.lookup(DepartmentWithoutNoArgConstructor.class);

		try {
			IDomainObject domainObject = domainClass.createTransient();
			fail("Expected exception to have been thrown.");
		} catch(ProgrammingModelException ex) {
			// expected
		}
	}

	

}
