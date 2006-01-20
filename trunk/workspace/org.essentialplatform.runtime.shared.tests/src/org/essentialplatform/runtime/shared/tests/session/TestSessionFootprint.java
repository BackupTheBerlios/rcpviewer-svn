package org.essentialplatform.runtime.shared.tests.session;

import java.util.List;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import org.essentialplatform.runtime.shared.tests.session.fixture.Department;
import org.essentialplatform.runtime.shared.tests.session.fixture.Employee;

public class TestSessionFootprint extends AbstractRuntimeClientTestCase  {

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
		IDomainObject<Department> hrDeptDomainObject = clientSession.create(deptDomainClass);
		Department pojo = hrDeptDomainObject.getPojo();
		pojo.setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = clientSession.create(deptDomainClass);
		pojo = itDeptDomainObject.getPojo();
		pojo.setName("IT");
	
		IDomainObject<Department> cateringDeptDomainObject = clientSession.create(deptDomainClass);
		pojo = cateringDeptDomainObject.getPojo();
		pojo.setName("Catering");
	
		List<IDomainObject<?>> departmentDomainObjects = clientSession.footprintFor(deptDomainClass);
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
		IDomainObject<Department> hrDeptDomainObject = clientSession.create(deptDomainClass);
		Department pojo = hrDeptDomainObject.getPojo();
		pojo.setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = clientSession.create(deptDomainClass);
		pojo = itDeptDomainObject.getPojo();
		pojo.setName("IT");
	
		IDomainObject<Department> cateringDeptDomainObject = clientSession.create(deptDomainClass);
		pojo = cateringDeptDomainObject.getPojo();
		pojo.setName("Catering");
	
		clientSession.detach(itDeptDomainObject);
	
		List<IDomainObject<?>> departmentDomainObjects = clientSession.footprintFor(deptDomainClass);
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
		
		IDomainObject<Department> hrDeptDomainObject = clientSession.create(deptDomainClass);
		Department pojo = hrDeptDomainObject.getPojo();
		pojo.setName("HR");
		
		IDomainObject<Department> itDeptDomainObject = clientSession.create(deptDomainClass);
		pojo = itDeptDomainObject.getPojo();
		pojo.setName("IT");
	
		IDomainObject<Department> cateringDeptDomainObject = clientSession.create(deptDomainClass);
		pojo = cateringDeptDomainObject.getPojo();
		pojo.setName("Catering");
	
		IDomainClass employeeDomainClass = lookupAny(Employee.class);
		IDomainObject<Employee> clarkKentEmployeeDomainObject = clientSession.create(employeeDomainClass);
		
		Employee clarkKent = clarkKentEmployeeDomainObject.getPojo();
		clarkKent.setFirstName("Clark");
		clarkKent.setSurname("Kent");
		
		IDomainObject<Employee> loisLaneEmployeeDomainObject = clientSession.create(employeeDomainClass);
		Employee loisLane = loisLaneEmployeeDomainObject.getPojo();
		loisLane.setFirstName("Lois");
		loisLane.setSurname("Lane");
		
		List<IDomainObject<?>> departmentDomainObjects = 
			clientSession.footprintFor(deptDomainClass);
		assertEquals(3, departmentDomainObjects.size());
		assertTrue(departmentDomainObjects.contains(hrDeptDomainObject));
		assertTrue(departmentDomainObjects.contains(itDeptDomainObject));
		assertTrue(departmentDomainObjects.contains(cateringDeptDomainObject));
	}



}
