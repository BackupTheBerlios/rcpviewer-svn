package org.essentialplatform.session;

import java.util.List;

import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.domain.IDomainClass;

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
		IDomainObject<?> hrDeptDomainObject = session.create(deptDomainClass);
		Department pojo = (Department)hrDeptDomainObject.getPojo();
		pojo.setName("HR");
		
		IDomainObject<?> itDeptDomainObject = session.create(deptDomainClass);
		pojo = (Department)itDeptDomainObject.getPojo();
		pojo.setName("IT");
	
		IDomainObject<?> cateringDeptDomainObject = session.create(deptDomainClass);
		pojo = (Department)cateringDeptDomainObject.getPojo();
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
		IDomainObject<?> hrDeptDomainObject = session.create(deptDomainClass);
		Department pojo = (Department)hrDeptDomainObject.getPojo();
		pojo.setName("HR");
		
		IDomainObject<?> itDeptDomainObject = session.create(deptDomainClass);
		pojo = (Department)itDeptDomainObject.getPojo();
		pojo.setName("IT");
	
		IDomainObject<?> cateringDeptDomainObject = session.create(deptDomainClass);
		pojo = (Department)cateringDeptDomainObject.getPojo();
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
		
		IDomainObject<?> hrDeptDomainObject = session.create(deptDomainClass);
		Department pojo = (Department)hrDeptDomainObject.getPojo();
		pojo.setName("HR");
		
		IDomainObject<?> itDeptDomainObject = session.create(deptDomainClass);
		pojo = (Department)itDeptDomainObject.getPojo();
		pojo.setName("IT");
	
		IDomainObject<?> cateringDeptDomainObject = session.create(deptDomainClass);
		pojo = (Department)cateringDeptDomainObject.getPojo();
		pojo.setName("Catering");
	
		IDomainClass employeeDomainClass = lookupAny(Employee.class);
		IDomainObject<?> clarkKentEmployeeDomainObject = session.create(employeeDomainClass);
		
		Employee clarkKent = (Employee)clarkKentEmployeeDomainObject.getPojo();
		clarkKent.setFirstName("Clark");
		clarkKent.setSurname("Kent");
		
		IDomainObject<?> loisLaneEmployeeDomainObject = session.create(employeeDomainClass);
		Employee loisLane = (Employee)loisLaneEmployeeDomainObject.getPojo();
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
