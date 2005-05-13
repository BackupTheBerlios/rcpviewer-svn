package de.berlios.rcpviewer.session;

import java.util.List;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.metamodel.MetaModel;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.impl.Department;
import de.berlios.rcpviewer.session.local.Session;

public class TestSession extends AbstractTestCase  {

	private static class MySessionListener implements ISessionListener {
		boolean attachedCallbackCalled = false;
		boolean detachedCallbackCalled = false;
		public void domainObjectAttached(SessionObjectEvent event) {
			attachedCallbackCalled=true;
		}
		public void domainObjectDetached(SessionObjectEvent event) {
			detachedCallbackCalled=true;
		}
	}


	private MetaModel metaModel;
	private ISession session;
	private IObjectStore objectStore;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = new MetaModel();
		session = new Session();
		objectStore = new InMemoryObjectStore();
		((Session)session).setObjectStore(objectStore);
	}

	protected void tearDown() throws Exception {
		objectStore.reset();
		objectStore = null;
		session.reset();
		session = null;
		metaModel = null;
		MetaModel.threadInstance().reset();
		super.tearDown();
	}

	
	/**
	 * Can instantiate domain object/pojo from Session.
	 * 
	 * <p>
	 * The returned object will be attached to any session.
	 */
	public void testCanInstantiateDomainObjectFromSession() {
		IDomainClass<Department> domainClass = 
			MetaModel.threadInstance().register(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertNotNull(domainObject);
		assertSame(domainObject.getDomainClass(), domainClass);
		Department pojo = domainObject.getPojo();
		assertNotNull(pojo);
		assertSame(Department.class, pojo.getClass());
		assertTrue(session.isAttached(domainObject));
		assertTrue(session.isAttached(domainObject.getPojo()));
	}


	/**
	 * Listeners are notified when an object is created, because the objects
	 * are initially attached to the session.
	 * 
	 * <p>
	 * The returned object will be attached to any session.
	 */
	public void testSessionListenersNotifiedThatInstantiatedDomainObjectAreAttached() {
		IDomainClass<Department> domainClass = 
			MetaModel.threadInstance().register(Department.class);
		MySessionListener l = session.addSessionListener(new MySessionListener());
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertTrue(l.attachedCallbackCalled);
		assertFalse(l.detachedCallbackCalled);
	}


	/**
	 * Can create a domain object through the session, but it will not be
	 * persistent. 
	 */
	public void testDomainObjectInitiallyTransient() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertFalse(domainObject.isPersistent());
	}


	public void testCanDetachFromSessionThroughDomainObject() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertTrue(session.isAttached(domainObject));
		assertTrue(session.isAttached(domainObject.getPojo()));
		session.detach(domainObject);
		assertFalse(session.isAttached(domainObject));
		assertFalse(session.isAttached(domainObject.getPojo()));
	}

	
	public void testCanDetachFromSessionThroughPojo() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertTrue(session.isAttached(domainObject));
		assertTrue(session.isAttached(domainObject.getPojo()));
		session.detach(domainObject.getPojo());
		assertFalse(session.isAttached(domainObject));
		assertFalse(session.isAttached(domainObject.getPojo()));
	}
	
	public void testDetachFromSesionNotifiesListeners() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		MySessionListener l = session.addSessionListener(new MySessionListener());
		session.detach(domainObject);
		assertFalse(l.attachedCallbackCalled);
		assertTrue(l.detachedCallbackCalled);
	}
	
	public void testCanReAttachFromSessionThroughDomainObject() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		session.detach(domainObject);
		assertFalse(session.isAttached(domainObject));
		assertFalse(session.isAttached(domainObject.getPojo()));
		session.attach(domainObject);
		assertTrue(session.isAttached(domainObject));
		assertTrue(session.isAttached(domainObject.getPojo()));
	}
	
	
	public void testCanReAttachFromSessionThroughPojo() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		session.detach(domainObject);
		assertFalse(session.isAttached(domainObject));
		assertFalse(session.isAttached(domainObject.getPojo()));
		session.attach(domainObject.getPojo());
		assertTrue(session.isAttached(domainObject));
		assertTrue(session.isAttached(domainObject.getPojo()));
	}

	
	public void testCannotDetachFromSessionIfAlreadyDetached() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		session.detach(domainObject);
		assertFalse(session.isAttached(domainObject));
		try {
			session.detach(domainObject);
			fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	
	public void testCannotAttachToSessionIfAlreadyAttached() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		assertTrue(session.isAttached(domainObject));
		try {
			session.attach(domainObject);
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
			MetaModel.threadInstance().register(Department.class);
		IDomainObject<Department> hrDeptDomainObject = 
			(IDomainObject<Department>)session.createTransient(deptDomainClass);
		hrDeptDomainObject.getPojo().setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = 
			(IDomainObject<Department>)session.createTransient(deptDomainClass);
		itDeptDomainObject.getPojo().setName("IT");

		IDomainObject<Department> cateringDeptDomainObject = 
			(IDomainObject<Department>)session.createTransient(deptDomainClass);
		cateringDeptDomainObject.getPojo().setName("Catering");


		IDomainClass<Employee> employeeDomainClass = 
			metaModel.register(Employee.class);
		IDomainObject<Employee> clarkKentEmployeeDomainObject = 
			(IDomainObject<Employee>)session.createTransient(employeeDomainClass);
		Employee clarkKent = clarkKentEmployeeDomainObject.getPojo();
		clarkKent.setFirstName("Clark");
		clarkKent.setSurname("Kent");
		
		IDomainObject<Employee> loisLaneEmployeeDomainObject = 
			(IDomainObject<Employee>)session.createTransient(employeeDomainClass);
		Employee loisLane = loisLaneEmployeeDomainObject.getPojo();
		loisLane.setFirstName("Lois");
		loisLane.setSurname("Lane");
		
		List<IDomainObject<?>> departmentDomainObjects = 
			session.footprintFor(deptDomainClass);
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
			MetaModel.threadInstance().register(Department.class);
		IDomainObject<Department> hrDeptDomainObject = 
			(IDomainObject<Department>)session.createTransient(deptDomainClass);
		hrDeptDomainObject.getPojo().setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = 
			(IDomainObject<Department>)session.createTransient(deptDomainClass);
		itDeptDomainObject.getPojo().setName("IT");

		IDomainObject<Department> cateringDeptDomainObject = 
			(IDomainObject<Department>)session.createTransient(deptDomainClass);
		cateringDeptDomainObject.getPojo().setName("Catering");

		session.detach(itDeptDomainObject);

		List<IDomainObject<?>> departmentDomainObjects = 
			session.footprintFor(deptDomainClass);
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
			metaModel.register(Department.class);
		IDomainObject<Department> hrDeptDomainObject = 
			(IDomainObject<Department>)session.createTransient(deptDomainClass);
		hrDeptDomainObject.getPojo().setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = 
			(IDomainObject<Department>)session.createTransient(deptDomainClass);
		itDeptDomainObject.getPojo().setName("IT");

		IDomainObject<Department> cateringDeptDomainObject = 
			(IDomainObject<Department>)session.createTransient(deptDomainClass);
		cateringDeptDomainObject.getPojo().setName("Catering");

		List<IDomainObject<?>> departmentDomainObjects = 
			session.footprintFor(deptDomainClass);
		try {
			departmentDomainObjects.remove(itDeptDomainObject);
			fail("Expected UnsupportedOperationException");
		} catch(UnsupportedOperationException ex) {
			// expected
		}
	}

	public void testCanRetrieveOncePersisted() {
		session = Session.instance(); // since Aspect will use singleton.
		objectStore = ((Session)session).getObjectStore();
		
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		Department dept = domainObject.getPojo();
		dept.setName("HR"); // name is used in Department's toString() -> title
		domainObject.persist();
		Department dept2 = 
			(Department)objectStore.findByTitle(Department.class, "HR");
		assertSame(dept2, dept);
		IDomainObject domainObject2 = session.getWrapper().wrapped(dept2);
		assertSame(domainObject2, domainObject);
	}



}
