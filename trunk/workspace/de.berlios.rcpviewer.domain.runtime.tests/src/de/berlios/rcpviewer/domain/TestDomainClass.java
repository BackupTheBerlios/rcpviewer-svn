package de.berlios.rcpviewer.domain;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
import de.berlios.rcpviewer.session.IDomainObject;

public class TestDomainClass extends AbstractRuntimeTestCase  {

	public TestDomainClass() {
		super(null);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}


	public void testCreateTransientCreatesUnderlyingPojo() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		
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
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		
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
		IRuntimeDomainClass<DepartmentWithoutNoArgConstructor> domainClass = 
			(IRuntimeDomainClass<DepartmentWithoutNoArgConstructor>)lookupAny(DepartmentWithoutNoArgConstructor.class);

		try {
			IDomainObject<DepartmentWithoutNoArgConstructor> domainObject = 
				domainClass.createTransient();
			fail("Expected exception to have been thrown.");
		} catch(ProgrammingModelException ex) {
			// expected
		}
	}

	

}
