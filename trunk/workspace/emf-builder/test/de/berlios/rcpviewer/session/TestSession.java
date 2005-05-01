package de.berlios.rcpviewer.session;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;

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

public class TestSession extends AbstractTestCase  {

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
	 * 
	 */
	public void testDomainObjectInitiallyTransient() {
		IDomainClass<Department> domainClass = 
			DomainClassRegistry.instance().register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		assertFalse(domainObject.isPersistent());
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
