package de.berlios.rcpviewer.facade;

import java.util.List;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.DomainClassRegistry;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.IObjectStoreAware;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
import de.berlios.rcpviewer.progmodel.standard.impl.Department;
import de.berlios.rcpviewer.session.IWrapper;
import de.berlios.rcpviewer.session.local.Session;
import junit.framework.TestCase;

public class TestFacade extends AbstractTestCase  {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		getObjectStore().reset();
		getSession().reset();
	}

	/**
	 * Can instantiate domain object/pojo from Session.
	 * 
	 * <p>
	 * The returned object will be attached to any session.
	 */
	public void testCanInstantiateDomainObjectFromSession() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		assertNotNull(domainObject);
		assertSame(domainObject.getDomainClass(), domainClass);
		Department pojo = domainObject.getPojo();
		assertNotNull(pojo);
		assertSame(Department.class, pojo.getClass());
		assertTrue(getSession().isAttached(domainObject));
		assertTrue(getSession().isAttached(domainObject.getPojo()));
	}

	/**
	 * Can instantiate domain object/pojo directly from DomainClass.
	 * 
	 * <p>
	 * The returned object will not be attached to any session.
	 */
	public void testCanInstantiateDomainObjectFromDomainClass() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		
		IDomainObject<Department> domainObject = domainClass.createTransient();
		assertNotNull(domainObject);
		assertSame(domainObject.getDomainClass(), domainClass);
		Department pojo = domainObject.getPojo();
		assertNotNull(pojo);
		assertSame(Department.class, pojo.getClass());
		assertFalse(getSession().isAttached(domainObject));
		assertFalse(getSession().isAttached(domainObject.getPojo()));
	}


	/**
	 * Cannot instantiate if don't play by the rules.
	 */
	public void testCannotInstantiateDomainObjectWithoutNoArgConstructor() {
		IDomainClass<DepartmentWithoutNoArgConstructor> domainClass = 
			DomainClassRegistry.instance().register(DepartmentWithoutNoArgConstructor.class);

		try {
			IDomainObject domainObject = domainClass.createTransient();
			fail("Expected exception to have been thrown.");
		} catch(ProgrammingModelException ex) {
			// expected
		}
	}

	
	public void testCanDetachFromSessionThroughDomainObject() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		assertTrue(getSession().isAttached(domainObject));
		assertTrue(getSession().isAttached(domainObject.getPojo()));
		getSession().detach(domainObject);
		assertFalse(getSession().isAttached(domainObject));
		assertFalse(getSession().isAttached(domainObject.getPojo()));
	}

	
	public void testCanDetachFromSessionThroughPojo() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		assertTrue(getSession().isAttached(domainObject));
		assertTrue(getSession().isAttached(domainObject.getPojo()));
		getSession().detach(domainObject.getPojo());
		assertFalse(getSession().isAttached(domainObject));
		assertFalse(getSession().isAttached(domainObject.getPojo()));
	}
	
	public void testCanReAttachFromSessionThroughDomainObject() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		getSession().detach(domainObject);
		assertFalse(getSession().isAttached(domainObject));
		assertFalse(getSession().isAttached(domainObject.getPojo()));
		getSession().attach(domainObject);
		assertTrue(getSession().isAttached(domainObject));
		assertTrue(getSession().isAttached(domainObject.getPojo()));
	}
	
	
	public void testCanReAttachFromSessionThroughPojo() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		getSession().detach(domainObject);
		assertFalse(getSession().isAttached(domainObject));
		assertFalse(getSession().isAttached(domainObject.getPojo()));
		getSession().attach(domainObject.getPojo());
		assertTrue(getSession().isAttached(domainObject));
		assertTrue(getSession().isAttached(domainObject.getPojo()));
	}

	
	public void testCannotDetachFromSessionIfAlreadyDetached() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		getSession().detach(domainObject);
		assertFalse(getSession().isAttached(domainObject));
		try {
			getSession().detach(domainObject);
			fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	
	public void testCannotAttachToSessionIfAlreadyAttached() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		assertTrue(getSession().isAttached(domainObject));
		try {
			getSession().attach(domainObject);
			fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	
	/**
	 * Set up 3 departments, 2 employees, then get the footprint for one;
	 * should exclude the other. 
	 */
	public void testSessionFootprint() {
		
		IDomainClass<Department> deptDomainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> hrDeptDomainObject = 
			(IDomainObject<Department>)getSession().createTransient(deptDomainClass);
		hrDeptDomainObject.getPojo().setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = 
			(IDomainObject<Department>)getSession().createTransient(deptDomainClass);
		itDeptDomainObject.getPojo().setName("IT");

		IDomainObject<Department> cateringDeptDomainObject = 
			(IDomainObject<Department>)getSession().createTransient(deptDomainClass);
		cateringDeptDomainObject.getPojo().setName("Catering");


		IDomainClass<Employee> employeeDomainClass = 
			DomainClassRegistry.instance().register(Employee.class);
		IDomainObject<Employee> clarkKentEmployeeDomainObject = 
			(IDomainObject<Employee>)getSession().createTransient(employeeDomainClass);
		Employee clarkKent = clarkKentEmployeeDomainObject.getPojo();
		clarkKent.setFirstName("Clark");
		clarkKent.setSurname("Kent");
		
		IDomainObject<Employee> loisLaneEmployeeDomainObject = 
			(IDomainObject<Employee>)getSession().createTransient(employeeDomainClass);
		Employee loisLane = loisLaneEmployeeDomainObject.getPojo();
		loisLane.setFirstName("Lois");
		loisLane.setSurname("Lane");
		
		List<IDomainObject<?>> departmentDomainObjects = 
			getSession().footprintFor(deptDomainClass);
		assertEquals(3, departmentDomainObjects.size());
		assertTrue(departmentDomainObjects.contains(hrDeptDomainObject));
		assertTrue(departmentDomainObjects.contains(itDeptDomainObject));
		assertTrue(departmentDomainObjects.contains(cateringDeptDomainObject));
	}
	
	/**
	 * Set up 3 departments, then detach one.
	 */
	public void testSessionFootprintIgnoresDetached() {
		
		IDomainClass<Department> deptDomainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> hrDeptDomainObject = 
			(IDomainObject<Department>)getSession().createTransient(deptDomainClass);
		hrDeptDomainObject.getPojo().setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = 
			(IDomainObject<Department>)getSession().createTransient(deptDomainClass);
		itDeptDomainObject.getPojo().setName("IT");

		IDomainObject<Department> cateringDeptDomainObject = 
			(IDomainObject<Department>)getSession().createTransient(deptDomainClass);
		cateringDeptDomainObject.getPojo().setName("Catering");

		getSession().detach(itDeptDomainObject);

		List<IDomainObject<?>> departmentDomainObjects = 
			getSession().footprintFor(deptDomainClass);
		assertEquals(2, departmentDomainObjects.size());
		assertTrue(departmentDomainObjects.contains(hrDeptDomainObject));
		assertFalse(departmentDomainObjects.contains(itDeptDomainObject)); // not in footprint
		assertTrue(departmentDomainObjects.contains(cateringDeptDomainObject));
	}
	
	
	/**
	 * 
	 */
	public void testSessionFootprintIsImmutable() {
		
		IDomainClass<Department> deptDomainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> hrDeptDomainObject = 
			(IDomainObject<Department>)getSession().createTransient(deptDomainClass);
		hrDeptDomainObject.getPojo().setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = 
			(IDomainObject<Department>)getSession().createTransient(deptDomainClass);
		itDeptDomainObject.getPojo().setName("IT");

		IDomainObject<Department> cateringDeptDomainObject = 
			(IDomainObject<Department>)getSession().createTransient(deptDomainClass);
		cateringDeptDomainObject.getPojo().setName("Catering");

		List<IDomainObject<?>> departmentDomainObjects = 
			getSession().footprintFor(deptDomainClass);
		try {
			departmentDomainObjects.remove(itDeptDomainObject);
			fail("Expected UnsupportedOperationException");
		} catch(UnsupportedOperationException ex) {
			// expected
		}
	}

	/**
	 * 
	 */
	public void testDomainObjectInitiallyTransient() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		assertFalse(domainObject.isPersistent());
	}


	/**
	 * 
	 */
	public void testCanPersistThroughDomainObject() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
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
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		domainObject.getPojo().save();
		assertTrue(domainObject.isPersistent());
	}

	/**
	 * Create directly from DomainClass rather than from Session. 
	 */
	public void testCannotPersistIfNotAttachedToSession() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = domainClass.createTransient();
		assertFalse(getSession().isAttached(domainObject));
		try {
			domainObject.persist();
			fail("IllegalArgumentException should have been thrown.");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}


	public void testCannotPersistMoreThanOnce() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		domainObject.persist();
		try {
			domainObject.persist();
			fail("IllegalStateException should have been thrown.");
		} catch(IllegalStateException ex) {
			// expected
		}
	}

	public void testCanRetrieveOncePersisted() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		Department dept = domainObject.getPojo();
		dept.setName("HR"); // name is used in Department's toString() -> title
		domainObject.persist();
		Department dept2 = 
			(Department)getObjectStore().findByTitle(Department.class, "HR");
		assertSame(dept2, dept);
		IDomainObject domainObject2 = getSession().getWrapper().wrapped(dept2);
		assertSame(domainObject2, domainObject);
	}


}
