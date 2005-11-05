package org.essentialplatform.runtime.tests.domain;

import java.io.PrintWriter;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.progmodel.ProgrammingModelException;
import org.essentialplatform.core.fixture.domain.Department;
import org.essentialplatform.core.fixture.domain.DepartmentWithoutNoArgConstructor;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;

public class TestRuntimeDomainClass extends AbstractRuntimeTestCase  {

	public void testCreateTransientCreatesUnderlyingPojo() {
		IDomainClass domainClass = lookupAny(Department.class);
		
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
//		IDomainClass domainClass = lookupAny(Department.class);
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
		IDomainClass domainClass = lookupAny(DepartmentWithoutNoArgConstructor.class);

		try {
			IDomainObject<DepartmentWithoutNoArgConstructor> domainObject = session.create(domainClass);
			fail("Expected exception to have been thrown.");
		} catch(ProgrammingModelException ex) {
			// expected
		}
	}

	public void incompletetestSerializeEmfResourceSet() {
		IDomainClass domainClass = lookupAny(Department.class);

		domainClass.getDomain().serializeTo(new PrintWriter(System.out));
	}

}
