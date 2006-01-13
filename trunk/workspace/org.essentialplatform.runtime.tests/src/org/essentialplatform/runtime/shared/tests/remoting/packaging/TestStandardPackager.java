package org.essentialplatform.runtime.shared.tests.remoting.packaging;

import java.io.CharArrayReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.remoting.marshalling.xstream.XStreamMarshalling;
import org.essentialplatform.runtime.shared.remoting.packaging.standard.StandardPackager;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import org.essentialplatform.runtime.shared.transaction.ITransaction;
import java.io.Serializable;

public class TestStandardPackager extends AbstractRuntimeClientTestCase {

	private StandardPackager packager;

	public TestStandardPackager() {
		super(null);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		TransactionManager.instance().suspend();
		packager = new StandardPackager();
	}

	@Override
	public void tearDown() throws Exception {
		packager = null;
		TransactionManager.instance().resume();
		super.tearDown();
	}

	public void testPackagePojo() {
		IDomainClass departmentDC = lookupAny(Department.class);
		IDomainClass cityDC = lookupAny(City.class);
		
		IDomainObject<Department> departmentDO = session.create(departmentDC);
		Department departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
		departmentPojo.addEmployee("Joe", "Blow");
		departmentPojo.addEmployee("Mary", "Doe");

		IDomainObject<City> cityDO = session.create(cityDC);
		City cityPojo = cityDO.getPojo();
		cityPojo.setName("London");
		departmentPojo.setCity(cityPojo);
		
		Object packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = new XStreamMarshalling().marshal(packedPojo);
		
		// debugging only...
		dumpTo(marshalledPackedPojo, "test4.xml");


	}
	
	private void dumpTo(String marshalledObject, String filename) {
		
		try {
			FileWriter fw = new FileWriter(filename);
			IOUtils.copy(new CharArrayReader(marshalledObject.toCharArray()), fw);
			fw.flush();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
	}

}
