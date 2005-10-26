package de.berlios.rcpviewer.domain;

import java.io.PrintWriter;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.session.IDomainObject;

public class TestRuntimeDomainClass extends AbstractRuntimeTestCase  {

	public TestRuntimeDomainClass() {
		super(new ExtendedProgModelDomainBuilder());
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateTransientCreatesUnderlyingPojo() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		domain.addBuilder(getDomainBuilder());
		domain.done();
		
		IDomainObject<Department> domainObject = 
			session.create(domainClass);
		assertNotNull(domainObject.getPojo());
		assertSame(Department.class, domainObject.getPojo().getClass());
	}


	// COMMENTED OUT BECAUSE THIS FUNCTIONALITY IS BEING RELOCATED INTO SESSION
	// (OR AT LEAST, OUT OF DOMAINCLASS).
//	/**
//	 * Can instantiate domain object/pojo directly from DomainClass.
//	 * 
//	 * <p>
//	 * The returned object will not be attached to any session.
//	 */
//	public void testCanInstantiateDomainObjectFromDomainClass() {
//		IDomainClass domainClass = 
//			(IDomainClass)lookupAny(Department.class);
//		domain.addBuilder(getDomainBuilder());
//		domain.done();
//		
//		IDomainObject<Department> domainObject = domainClass.create(session);
//		assertNotNull(domainObject);
//		assertSame(domainObject.getDomainClass(), domainClass);
//		Department pojo = domainObject.getPojo();
//		assertNotNull(pojo);
//		assertSame(Department.class, pojo.getClass());
//		assertFalse(session.isAttached(domainObject));
//	}


	/**
	 * Cannot instantiate if don't play by the rules.
	 */
	public void testCannotInstantiateDomainObjectWithoutNoArgConstructor() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(DepartmentWithoutNoArgConstructor.class);
		domain.addBuilder(getDomainBuilder());
		domain.done();

		try {
			IDomainObject<DepartmentWithoutNoArgConstructor> domainObject = 
				session.create(domainClass);
			fail("Expected exception to have been thrown.");
		} catch(ProgrammingModelException ex) {
			// expected
		}
	}

	public void incompletetestSerializeEmfResourceSet() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		domain.addBuilder(getDomainBuilder());
		domain.done();

		domainClass.getDomain().serializeTo(new PrintWriter(System.out));
	}

}
