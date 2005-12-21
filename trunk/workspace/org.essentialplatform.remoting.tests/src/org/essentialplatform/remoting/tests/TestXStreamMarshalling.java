package org.essentialplatform.remoting.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.remoting.IRemoting;
import org.essentialplatform.remoting.marshalling.IMarshalling;
import org.essentialplatform.remoting.marshalling.xstream.XStreamMarshalling;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;
import org.essentialplatform.runtime.transaction.ITransaction;

public class TestXStreamMarshalling extends AbstractRuntimeTestCase {

	private IDomainClass domainClass;
	
	private IMarshalling remoting;

	public TestXStreamMarshalling() {
		super(null);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		remoting = new XStreamMarshalling();
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
		remoting.marshalTo(dobj.getPojo(), baos);
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
		remoting.marshalTo(xactn, baos);
		
		// debugging only...
		// dumpTo(baos.toByteArray(), "test2.xml");

		Object obj = remoting.unmarshalFrom(new ByteArrayInputStream(baos.toByteArray()));
		
		assertTrue(obj instanceof ITransaction);
		ITransaction unmarshalledXactn = (ITransaction)obj;
		assertEquals(xactn.getInstantiatedPojos().size(), unmarshalledXactn.getInstantiatedPojos().size());
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
