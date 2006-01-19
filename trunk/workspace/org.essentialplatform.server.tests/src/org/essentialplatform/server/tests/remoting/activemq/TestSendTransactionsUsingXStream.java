package org.essentialplatform.server.tests.remoting.activemq;

import java.util.concurrent.SynchronousQueue;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.activemq.ActiveMQConnection;
import org.activemq.ActiveMQConnectionFactory;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.server.remoting.activemq.ActiveMqRemotingServer;
import org.essentialplatform.runtime.server.remoting.activemq.ActiveMqServerConstants;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.enqueue.EnqueuingTransactionProcessor;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import org.essentialplatform.runtime.shared.transaction.ITransaction;

public class TestSendTransactionsUsingXStream extends AbstractRuntimeClientTestCase {

	private IDomainClass domainClass;
	
	private ActiveMqRemotingServer remotingServer;
	private SynchronousQueue<ITransaction> processedTransactions;
	private EnqueuingTransactionProcessor transactionProcessor;
	
	public TestSendTransactionsUsingXStream() {
		super(null);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		remotingServer = new ActiveMqRemotingServer();
		remotingServer.setMessageListenerEnabled(true);
		processedTransactions = new SynchronousQueue<ITransaction>();
		transactionProcessor = new EnqueuingTransactionProcessor(processedTransactions);
		remotingServer.setTransactionProcessor(transactionProcessor);
		remotingServer.start();
	}

	@Override
	protected void tearDown() throws Exception {
		processedTransactions = null;
		transactionProcessor = null;
		if (remotingServer != null) {
			remotingServer.shutdown();
		}
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
        ActiveMQConnectionFactory connectionFactory = 
        	new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(ActiveMqServerConstants.TRANSACTION_QUEUE);
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		String marshalledXactn = this.remotingServer.getMarshalling().marshal(xactn);
        
        TextMessage message = session.createTextMessage(marshalledXactn);
        producer.send(message);
        session.close();
        connection.close();

        // see if our object came through
        ITransaction unmarshalledXactn = processedTransactions.take();
		assertEquals(xactn.getInstantiatedPojos().size(), unmarshalledXactn.getInstantiatedPojos().size());
		assertEquals(xactn.getCommittedChanges(), xactn.getCommittedChanges()); // value semantics for changes.

	            
	}

}
