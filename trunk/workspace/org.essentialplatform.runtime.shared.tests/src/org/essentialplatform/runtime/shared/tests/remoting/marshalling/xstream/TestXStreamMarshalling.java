package org.essentialplatform.runtime.shared.tests.remoting.marshalling.xstream;

import java.io.CharArrayReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.client.remoting.packaging.standard.StandardPackager;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.remoting.marshalling.xstream.XStreamMarshalling;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;

public class TestXStreamMarshalling extends AbstractRuntimeClientTestCase {

	private IDomainClass domainClass;
	
	private XStreamMarshalling marshalling;
	private StandardPackager packager;

	public TestXStreamMarshalling() {
		super(null);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		marshalling = new XStreamMarshalling();
		packager = new StandardPackager();
	}

	@Override
	public void tearDown() throws Exception {
		marshalling = null;
		packager = null;
		domainClass = null;
		super.tearDown();
	}

	public void testMarshallTransactionWithInstantiationChange() {
		domainClass = lookupAny(CustomerWithPrimitiveAttributes.class);
		
		IDomainObject<CustomerWithPrimitiveAttributes> dobj = clientSession.create(domainClass);
		dobj.getPojo().setInteger(23);

		// TODO: not yet complete
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
		
		IDomainObject<Department> departmentDO = clientSession.create(departmentDC);
		Department departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
		departmentPojo.addEmployee("Joe", "Blow");
		departmentPojo.addEmployee("Mary", "Doe");

		// TODO: should also have a OneToOneReference change here

		ITransaction xactn = transactionManager.getCurrentTransactionFor(departmentPojo);
		transactionManager.commit(departmentPojo);

		ITransactionPackage packagedXactn = packager.pack(xactn);
		packager.optimize(marshalling);
		String marshalledPackagedXactn = marshalling.marshal(packagedXactn);
		
		// debugging only...
		dumpTo(marshalledPackagedXactn, "test3.xml");

		Object obj = marshalling.unmarshal(marshalledPackagedXactn);
		
		assertTrue(obj instanceof ITransactionPackage);
		
		ITransactionPackage unmarshalledPackagedXactn = (ITransactionPackage)obj;
		assertEquals(xactn.getEnlistedPojos().size(), unmarshalledPackagedXactn.enlistedPojos().size());
		assertEquals(xactn.getCommittedChanges(), xactn.getCommittedChanges()); // value semantics for changes.
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
