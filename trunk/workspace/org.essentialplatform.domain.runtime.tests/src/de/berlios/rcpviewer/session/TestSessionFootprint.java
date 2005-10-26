package de.berlios.rcpviewer.session;

import java.util.List;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IDomainClass;

public class TestSessionFootprint extends AbstractRuntimeTestCase  {

	public TestSessionFootprint() {
		super(null);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testSessionFootprintIsImmutable() {
		
		IDomainClass deptDomainClass = 
			(IDomainClass)lookupAny(Department.class);
		IDomainObject<Department> hrDeptDomainObject = 
			(IDomainObject<Department>)session.create(deptDomainClass);
		hrDeptDomainObject.getPojo().setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = 
			(IDomainObject<Department>)session.create(deptDomainClass);
		itDeptDomainObject.getPojo().setName("IT");
	
		IDomainObject<Department> cateringDeptDomainObject = 
			(IDomainObject<Department>)session.create(deptDomainClass);
		cateringDeptDomainObject.getPojo().setName("Catering");
	
		List<IDomainObject<Department>> departmentDomainObjects = 
			session.footprintFor(deptDomainClass);
		try {
			departmentDomainObjects.remove(itDeptDomainObject);
			fail("Expected UnsupportedOperationException");
		} catch(UnsupportedOperationException ex) {
			// expected
		}
	}

	/**
	 * Set up 3 departments, then detach one.
	 */
	public void testSessionFootprintIgnoresDetached() {
		
		IDomainClass deptDomainClass = 
			(IDomainClass)lookupAny(Department.class);
		IDomainObject<Department> hrDeptDomainObject = 
			(IDomainObject<Department>)session.create(deptDomainClass);
		hrDeptDomainObject.getPojo().setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = 
			(IDomainObject<Department>)session.create(deptDomainClass);
		itDeptDomainObject.getPojo().setName("IT");
	
		IDomainObject<Department> cateringDeptDomainObject = 
			(IDomainObject<Department>)session.create(deptDomainClass);
		cateringDeptDomainObject.getPojo().setName("Catering");
	
		session.detach(itDeptDomainObject);
	
		List<IDomainObject<Department>> departmentDomainObjects = 
			session.footprintFor(deptDomainClass);
		assertEquals(2, departmentDomainObjects.size());
		assertTrue(departmentDomainObjects.contains(hrDeptDomainObject));
		assertFalse(departmentDomainObjects.contains(itDeptDomainObject)); // not in footprint
		assertTrue(departmentDomainObjects.contains(cateringDeptDomainObject));
	}

	/**
	 * Set up 3 departments, 2 employees, then get the footprint for one;
	 * should exclude the other. 
	 */
	public void testSessionFootprint() {
		
		IDomainClass deptDomainClass = 
			(IDomainClass)lookupAny(Department.class);
		
		IDomainObject<Department> hrDeptDomainObject = 
			(IDomainObject<Department>)session.create(deptDomainClass);
		hrDeptDomainObject.getPojo().setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = 
			(IDomainObject<Department>)session.create(deptDomainClass);
		itDeptDomainObject.getPojo().setName("IT");
	
		IDomainObject<Department> cateringDeptDomainObject = 
			(IDomainObject<Department>)session.create(deptDomainClass);
		cateringDeptDomainObject.getPojo().setName("Catering");
	
	
		IDomainClass employeeDomainClass = 
			(IDomainClass)lookupAny(Employee.class);
		IDomainObject<Employee> clarkKentEmployeeDomainObject = 
			(IDomainObject<Employee>)session.create(employeeDomainClass);
		Employee clarkKent = clarkKentEmployeeDomainObject.getPojo();
		clarkKent.setFirstName("Clark");
		clarkKent.setSurname("Kent");
		
		IDomainObject<Employee> loisLaneEmployeeDomainObject = 
			(IDomainObject<Employee>)session.create(employeeDomainClass);
		Employee loisLane = loisLaneEmployeeDomainObject.getPojo();
		loisLane.setFirstName("Lois");
		loisLane.setSurname("Lane");
		
		List<IDomainObject<Department>> departmentDomainObjects = 
			session.footprintFor(deptDomainClass);
		assertEquals(3, departmentDomainObjects.size());
		assertTrue(departmentDomainObjects.contains(hrDeptDomainObject));
		assertTrue(departmentDomainObjects.contains(itDeptDomainObject));
		assertTrue(departmentDomainObjects.contains(cateringDeptDomainObject));
	}



}
