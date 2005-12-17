package org.essentialplatform.runtime.tests.distribution;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.IOUtils;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.distribution.IDistribution;
import org.essentialplatform.runtime.distribution.IMarshaller;
import org.essentialplatform.runtime.distribution.XStreamDistribution;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;
import org.essentialplatform.runtime.transaction.ITransaction;

public class TestXStreamDistribution extends AbstractRuntimeTestCase {

	private IDomainClass domainClass;
	
	private IDistribution distribution;

	public TestXStreamDistribution() {
		super(null);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		distribution = new XStreamDistribution();
	}

	@Override
	public void tearDown() throws Exception {
		domainClass = null;
		super.tearDown();
	}

	public void testMarshallTransactionWithInstantiationChange() {
		domainClass = lookupAny(CustomerWithPrimitiveAttributes.class);
		
		IDomainObject<CustomerWithPrimitiveAttributes> dobj = session.create(domainClass);
		dobj.getPojo().setInteger(23);

		ByteArrayOutputStream baos;
		baos = new ByteArrayOutputStream();
		distribution.marshalTo(dobj.getPojo(), baos);
	}

	public void testMarshallTransactionWithAttributeChange() {
		
	}

	public void testMarshallTransactionWithAssociateReferenceChange() {
		
	}

	public void testMarshallTransactionWithDissociateReferenceChange() {
		
	}

	public void testMarshallTransactionWithAddToCollectionChange() {
		
	}

	public void testMarshallTransactionWithRemoveFromCollectionChange() {
		
	}

	public void testMarshallDeletionChange() {
		
	}

	// TODO: complete these tests, also create a plugin out of commons io so
	// that can dump the marshalled contents to a file.
	
	/**
	 * that is, create a bunch of transient objects, associate them and then 
	 * send 'em across the wire. 
	 */
	public void testMarshallTransactionWithTransientGraphCombinationOfChanges() {
		IDomainClass departmentDC = lookupAny(Department.class);
		IDomainClass employeeDC = lookupAny(Employee.class);
		
		IDomainObject<Department> departmentDO = session.create(departmentDC);
		Department departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
		departmentPojo.addEmployee("Joe", "Blow");
		departmentPojo.addEmployee("Mary", "Doe");

		// TODO: should also have a OneToOneReference change here

		ITransaction xactn = transactionManager.getCurrentTransactionFor(departmentPojo);
		transactionManager.commit(departmentPojo);

		ByteArrayOutputStream baos;
		baos = new ByteArrayOutputStream();
		distribution.marshalTo(xactn, baos);
		
		// debugging only...
		dumpTo(baos.toByteArray(), "test2.xml");

		Object obj = distribution.unmarshalFrom(new ByteArrayInputStream(baos.toByteArray()));
		
		assertTrue(obj instanceof ITransaction);
		ITransaction unmarshalledXactn = (ITransaction)obj;
		assertEquals(xactn.getEnlistedPojos().size(), unmarshalledXactn.getEnlistedPojos().size());
		assertEquals(xactn.getCommittedChanges(), xactn.getCommittedChanges()); // value semantics for changes.
		
	}
	
	private void dumpTo(byte[] bytes, String filename) {
		dumpTo(new ByteArrayInputStream(bytes), filename);
	}
	
	private void dumpTo(InputStream is, String filename) {
		
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			IOUtils.copy(is, fos);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
	}

}
