package org.essentialplatform.runtime.tests.distribution;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.distribution.IMarshaller;
import org.essentialplatform.runtime.distribution.XStreamMarshaller;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;

public class TestXStreamMarshaller extends AbstractRuntimeTestCase {

	private IDomainClass domainClass;
	
	private IMarshaller marshaller;

	public TestXStreamMarshaller() {
		super(null);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		marshaller = new XStreamMarshaller();
	}

	@Override
	public void tearDown() throws Exception {
		domainClass = null;
		super.tearDown();
	}

	public void testMarshallPrimitiveAttributes() {
		domainClass = lookupAny(CustomerWithPrimitiveAttributes.class);
		
		IDomainObject<CustomerWithPrimitiveAttributes> dobj = session.create(domainClass);
		dobj.getPojo().setInteger(23);

		ByteArrayOutputStream baos;
		baos = new ByteArrayOutputStream();
		marshaller.marshalTo(dobj.getPojo(), baos);

	}

	public void testMarshallCollection() {
		IDomainClass departmentDC = lookupAny(Department.class);
		IDomainClass employeeDC = lookupAny(Employee.class);
		
		IDomainObject<Department> departmentDO = session.create(departmentDC);
		Department departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
		departmentPojo.addEmployee("Joe", "Blow");
		departmentPojo.addEmployee("Mary", "Doe");
		transactionManager.commit(departmentPojo);

		ByteArrayOutputStream baos;
		baos = new ByteArrayOutputStream();
		marshaller.marshalTo(departmentPojo, baos);

	}

}
