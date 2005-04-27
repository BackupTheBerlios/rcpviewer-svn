package de.berlios.rcpviewer.facade;

import de.berlios.rcpviewer.metamodel.DomainClassRegistry;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
import de.berlios.rcpviewer.progmodel.standard.impl.Department;
import de.berlios.rcpviewer.session.local.Session;
import junit.framework.TestCase;

public class TestFacade extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		InMemoryObjectStore.instance().reset();
	}

	/**
	 * Can instantiate.
	 */
	public void testCanInstantiateDomainObject() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		
		IDomainObject<Department> domainObject = domainClass.createTransient();
		assertNotNull(domainObject);
		assertSame(domainObject.getDomainClass(), domainClass);
		Department pojo = domainObject.getPojo();
		assertNotNull(pojo);
		assertSame(Department.class, pojo.getClass());
		
	}

	/**
	 * Cannot instantiate if don't play by the rules.
	 */
	public void testCannotInstantiateDomainObjectWithoutNoArgConstructor() {
		IDomainClass domainClass = 
			DomainClassRegistry.instance().register(DepartmentWithoutNoArgConstructor.class);

		try {
			IDomainObject domainObject = domainClass.createTransient();
			fail("Expected exception to have been thrown.");
		} catch(ProgrammingModelException ex) {
			// expected
		}
	}

	/**
	 * 
	 */
	public void testDomainObjectInitiallyTransient() {
		IDomainClass domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<DepartmentWithoutNoArgConstructor> domainObject = 
											domainClass.createTransient();
		assertFalse(domainObject.isPersistent());
	}


	/**
	 * 
	 */
	public void testCanPersistThroughDomainObject() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = domainClass.createTransient();
		domainObject.persist();
		assertTrue(domainObject.isPersistent());
	}

	/**
	 * Depending upon the programming model, the pojo may request that it is
	 * persisted.
	 * 
	 * <p>
	 * The means for doing this will be dependent on the programming model.
	 * In the standard programming model, we pick up on a method called 'save'.
	 */
	public void testCanPersistThroughPojo() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = domainClass.createTransient();
		domainObject.getPojo().save();
		assertTrue(domainObject.isPersistent());
	}

	public void testCannotPersistMoreThanOnce() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = domainClass.createTransient();
		domainObject.persist();
		try {
			domainObject.persist();
			fail("IllegalStateException should have been thrown.");
		} catch(IllegalStateException ex) {
			// expected
		}
		InMemoryObjectStore.instance().findByTitle(Department.class, "HR");
	}

	public void testCanRetrieveOncePersisted() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = domainClass.createTransient();
		Department dept = domainObject.getPojo();
		dept.setName("HR"); // name is used in Department's toString() -> title
		domainObject.persist();
		Department dept2 = 
			(Department)InMemoryObjectStore.instance().findByTitle(Department.class, "HR");
		assertSame(dept2, dept);
		IDomainObject domainObject2 = Session.instance().getDomainObjectFor(dept2);
		assertSame(domainObject2, domainObject);
	}
}
