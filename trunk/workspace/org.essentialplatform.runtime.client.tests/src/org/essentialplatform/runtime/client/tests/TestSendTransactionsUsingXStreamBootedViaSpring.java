package org.essentialplatform.runtime.client.tests;

import java.util.concurrent.SynchronousQueue;

import javax.jms.JMSException;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.client.remoting.packaging.standard.StandardPackager;
import org.essentialplatform.runtime.client.remoting.transport.activemq.ActiveMqTransport;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.server.remoting.activemq.ActiveMqRemotingServer;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.enqueue.EnqueuingTransactionProcessor;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;

public class TestSendTransactionsUsingXStreamBootedViaSpring extends AbstractRuntimeClientTestCase {

	private IDomainClass domainClass;
	
	private ActiveMqRemotingServer remotingServer;
	private SynchronousQueue<ITransactionPackage> processedTransactions;
	private EnqueuingTransactionProcessor transactionProcessor;
    private StandardPackager packager;

	private ActiveMqTransport clientTransport;


	public TestSendTransactionsUsingXStreamBootedViaSpring() {
		super(null);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		remotingServer = new ActiveMqRemotingServer();
		remotingServer.setMessageListenerEnabled(true);
		processedTransactions = new SynchronousQueue<ITransactionPackage>();
		transactionProcessor = new EnqueuingTransactionProcessor(processedTransactions);
		packager = new StandardPackager();
		remotingServer.setTransactionProcessor(transactionProcessor);
		remotingServer.start();
		
		clientTransport = new ActiveMqTransport();
		clientTransport.start();
	}

	@Override
	protected void tearDown() throws Exception {
		if (clientTransport != null) {
			clientTransport.shutdown();
		}
		processedTransactions = null;
		transactionProcessor = null;
		if (remotingServer != null) {
			remotingServer.shutdown();
		}
		packager = null;
		super.tearDown();
	}
	


	public void testCanSendAndReceiveFromJmsQueueUsingMessageListener() throws InterruptedException, JMSException {
		
        // create a xactn
        IDomainClass departmentDC = lookupAny(Department.class);
		IDomainObject<Department> departmentDO = clientSession.create(departmentDC);
		Department departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
		departmentPojo.addEmployee("Joe", "Blow");
		departmentPojo.addEmployee("Mary", "Doe");

		ITransaction xactn = transactionManager.getCurrentTransactionFor(departmentPojo);
		transactionManager.commit(departmentPojo);

		// send it (this needs to go through a ClientRemoting object...)
        ITransactionPackage packagedXactn = packager.pack(xactn);
		String marshalledXactn = this.remotingServer.getMarshalling().marshal(packagedXactn);
		clientTransport.send(marshalledXactn);

        // see if our object came through
        ITransactionPackage unmarshalledXactnPackage = processedTransactions.take();
		assertEquals(xactn.getEnlistedPojos().size(), unmarshalledXactnPackage.enlistedPojos().size());
		assertEquals(xactn.getCommittedChanges(), xactn.getCommittedChanges()); // value semantics for changes.

	            
	}

}
