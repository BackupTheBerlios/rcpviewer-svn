package org.essentialplatform.server.tests.remoting.activemq;


import java.util.UUID;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.activemq.ActiveMQConnection;
import org.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.BasicConfigurator;
import org.essentialplatform.runtime.client.remoting.transport.activemq.ActiveMqTransport;
import org.essentialplatform.runtime.server.database.hsqldb.HsqlDatabaseServer;
import org.essentialplatform.runtime.server.remoting.activemq.ActiveMqRemotingServer;
import org.essentialplatform.runtime.shared.remoting.activemq.DestinationCleaner;
import org.essentialplatform.runtime.shared.util.SleepUtil;

import junit.framework.TestCase;

public class TestLocalSendReceive extends TestCase {

	private ActiveMqRemotingServer remotingServer;
	private ActiveMqTransport clientTransport;

    private final String _queue = "org.essentialplatform.receiver";

	@Override
	protected void setUp() throws Exception {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		super.setUp();

		remotingServer = new ActiveMqRemotingServer();
		remotingServer.setMessageListenerEnabled(false);
		remotingServer.start();
		
    	new DestinationCleaner().setDestinationName(_queue).clean();

		clientTransport = new ActiveMqTransport();
    	clientTransport.setDestinationName(_queue);
    	clientTransport.start();
    	
	}

	@Override
	protected void tearDown() throws Exception {
		if (clientTransport != null) {
			clientTransport.shutdown();
		}
		if (remotingServer != null) {
			remotingServer.shutdown();
		}
		super.tearDown();
	}
	
	public void testCanSendAndReceiveFromJmsQueue() throws InterruptedException {
		final String messageToSend = UUID.randomUUID().toString();
		class OneShotSender extends Thread {
			String failedReason;
			@Override
	        public void run() {
	            try {
	            	clientTransport.send(messageToSend);
	            } catch (Exception ex) {
	                failedReason = ex.getMessage();
	            }
	        }
		}
	    class OneShotReceiver extends Thread implements ExceptionListener {
	    	OneShotReceiver() {
                _connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
				try {
					_connection = _connectionFactory.createConnection();
					_connection.start();
	                System.out.println("OneShotReceiver: connected");
	                _connection.setExceptionListener(this);
	                _session = _connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
					_destination = _session.createQueue(_queue);
					_consumer = _session.createConsumer(_destination);
				} catch (JMSException ex) {
					throw new RuntimeException(ex);
				}
	    	}
			String failedReason;
			String messageReceived;
			private ActiveMQConnectionFactory _connectionFactory;
			private Connection _connection;
			private Session _session;
			private Destination _destination;
			private MessageConsumer _consumer;
	    	@Override
	        public void run() {
	            try {
	                Message message = _consumer.receive();
	                if (message instanceof TextMessage) {
	                    TextMessage textMessage = (TextMessage) message;
	                    messageReceived = textMessage.getText();
		                System.out.println("OneShotReceiver: message received");
	                } else {
	                    failedReason = "message received was not a TextMessage";
	                }
	                _session.commit();
	                _consumer.close();
	                _session.close();
	                _connection.close();
	                System.out.println("OneShotSender: connection closed");
	            } catch (Exception ex) {
	                failedReason = ex.getMessage();
	            }
	        }
			public void onException(JMSException ex) {
				failedReason = "Caught exception: " + ex.getMessage();
			}
	    }
		OneShotSender oneShotSender = new OneShotSender();
		OneShotReceiver oneShotReceiver = new OneShotReceiver();
		oneShotReceiver.start();
		oneShotSender.run();  // can just run synchronously
		assertNull(oneShotReceiver.failedReason, oneShotReceiver.failedReason);
		assertNull(oneShotReceiver.failedReason, oneShotSender.failedReason);
		oneShotReceiver.join();
		assertEquals(messageToSend, oneShotReceiver.messageReceived);
	}
	

}
