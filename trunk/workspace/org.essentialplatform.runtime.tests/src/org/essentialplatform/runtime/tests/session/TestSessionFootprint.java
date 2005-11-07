package org.essentialplatform.runtime.tests.session;

import java.util.List;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.fixture.session.Department;
import org.essentialplatform.runtime.fixture.session.Employee;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;

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
		
		IDomainClass deptDomainClass = lookupAny(Department.class);
		IDomainObject<Department> hrDeptDomainObject = session.create(deptDomainClass);
		Department pojo = hrDeptDomainObject.getPojo();
		pojo.setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = session.create(deptDomainClass);
		pojo = itDeptDomainObject.getPojo();
		pojo.setName("IT");
	
		IDomainObject<Department> cateringDeptDomainObject = session.create(deptDomainClass);
		pojo = cateringDeptDomainObject.getPojo();
		pojo.setName("Catering");
	
		List<IDomainObject<?>> departmentDomainObjects = session.footprintFor(deptDomainClass);
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
		
		IDomainClass deptDomainClass = lookupAny(Department.class);
		IDomainObject<Department> hrDeptDomainObject = session.create(deptDomainClass);
		Department pojo = hrDeptDomainObject.getPojo();
		pojo.setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = session.create(deptDomainClass);
		pojo = itDeptDomainObject.getPojo();
		pojo.setName("IT");
	
		IDomainObject<Department> cateringDeptDomainObject = session.create(deptDomainClass);
		pojo = cateringDeptDomainObject.getPojo();
		pojo.setName("Catering");
	
		session.detach(itDeptDomainObject);
	
		List<IDomainObject<?>> departmentDomainObjects = session.footprintFor(deptDomainClass);
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
		
		IDomainClass deptDomainClass = lookupAny(Department.class);
		
		IDomainObject<Department> hrDeptDomainObject = session.create(deptDomainClass);
		Department pojo = hrDeptDomainObject.getPojo();
		pojo.setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = session.create(deptDomainClass);
		pojo = itDeptDomainObject.getPojo();
		pojo.setName("IT");
	
		IDomainObject<Department> cateringDeptDomainObject = session.create(deptDomainClass);
		pojo = cateringDeptDomainObject.getPojo();
		pojo.setName("Catering");
	
		IDomainClass employeeDomainClass = lookupAny(Employee.class);
		IDomainObject<Employee> clarkKentEmployeeDomainObject = session.create(employeeDomainClass);
		
		Employee clarkKent = clarkKentEmployeeDomainObject.getPojo();
		clarkKent.setFirstName("Clark");
		clarkKent.setSurname("Kent");
		
		IDomainObject<Employee> loisLaneEmployeeDomainObject = session.create(employeeDomainClass);
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



}
