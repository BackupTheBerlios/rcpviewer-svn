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

public class TestLocalSendReceiveAsync extends TestCase {

	private ActiveMqRemotingServer remotingServer;
	private ActiveMqTransport clientTransport;

    private final String _queue = "org.essentialplatform.listener";

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
	
	

	public void testCanSendAndReceiveFromJmsQueueUsingMessageListener() throws InterruptedException, JMSException {
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
	    class OneShotListener implements MessageListener, ExceptionListener {
			String failedReason;
			String messageReceived;
			private ActiveMQConnectionFactory _connectionFactory;
			private Connection _connection;
			private Session _session;
			private MessageConsumer _consumer;
			private Destination _destination;

	        public OneShotListener() throws JMSException {
                _connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
				_connection = _connectionFactory.createConnection();
				_connection.start();
                System.out.println("OneShotListener: connected");
                _connection.setExceptionListener(this);
                _session = _connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
				_destination = _session.createQueue(_queue);
				_consumer = _session.createConsumer(_destination);
                
                System.out.println("OneShotListener: consumer obtained");
                
				_consumer.setMessageListener(this);
	        }
			public void onException(JMSException ex) {
				failedReason = "Caught exception: " + ex.getMessage();
			}
			public void onMessage(Message message) {
                try {
	                if (message instanceof TextMessage) {
	                    TextMessage textMessage = (TextMessage) message;
	                    messageReceived = textMessage.getText();
	                } else {
	                    failedReason = "message received was not a TextMessage";
	                }
	                _session.commit();
	                System.out.println("OneShotListener: message received");
					_consumer.close();
	                _session.close();
	                _connection.close();
	                System.out.println("OneShotListener: connection closed");
				} catch (JMSException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
	    }
		OneShotSender oneShotSender = new OneShotSender();
		OneShotListener oneShotReceiver = new OneShotListener();
		oneShotSender.run(); // can just run synchronously
		SleepUtil.sleepUninterrupted(50); // give listener time to process.
		assertNull(oneShotReceiver.failedReason, oneShotReceiver.failedReason);
		assertNull(oneShotSender.failedReason, oneShotSender.failedReason);
		assertEquals(messageToSend, oneShotReceiver.messageReceived);
	}
}
