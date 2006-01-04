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
import org.essentialplatform.runtime.server.database.hsqldb.HsqlDatabaseServer;
import org.essentialplatform.runtime.server.remoting.activemq.ActiveMqRemotingServer;

import junit.framework.TestCase;

public class TestLocalSendReceive extends TestCase {

	private ActiveMqRemotingServer remotingServer;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		remotingServer = new ActiveMqRemotingServer();
		remotingServer.setMessageListenerEnabled(false);
		remotingServer.start();
	}

	@Override
	protected void tearDown() throws Exception {
		if (remotingServer != null) {
			remotingServer.shutdown();
		}
		super.tearDown();
	}
	
	public void testCanSendAndReceiveFromJmsQueue() throws InterruptedException {
        final String queue = "org.essentialplatform.receiver";
		final String messageToSend = UUID.randomUUID().toString();
		class OneShotSender extends Thread {
			String failedReason;
			@Override
	        public void run() {
	            try {
	                ActiveMQConnectionFactory connectionFactory = 
	                	new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
	                Connection connection = connectionFactory.createConnection();
	                connection.start();
	                System.out.println("OneShotSender: connected");
	                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
					Destination destination = session.createQueue(queue);
	                MessageProducer producer = session.createProducer(destination);
	                System.out.println("OneShotSender: producer obtained");
	                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	                TextMessage message = session.createTextMessage(messageToSend);
	                producer.send(message);
	                System.out.println("OneShotSender: message sent");
	                session.close();
	                connection.close();
	                System.out.println("OneShotSender: connection closed");
	            } catch (Exception ex) {
	                failedReason = ex.getMessage();
	            }
	        }
		}
	    class OneShotReceiver extends Thread implements ExceptionListener {
			String failedReason;
			String messageReceived;
	    	@Override
	        public void run() {
	            try {
	                ActiveMQConnectionFactory connectionFactory = 
	                	new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
	                Connection connection = connectionFactory.createConnection();
	                connection.start();
	                System.out.println("OneShotReceiver: connected");
	                connection.setExceptionListener(this);
	                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	                Destination destination = session.createQueue(queue);
	                MessageConsumer consumer = session.createConsumer(destination);
	                System.out.println("OneShotReceiver: consumer obtained");
	                Message message = consumer.receive();
	                if (message instanceof TextMessage) {
	                    TextMessage textMessage = (TextMessage) message;
	                    messageReceived = textMessage.getText();
		                System.out.println("OneShotReceiver: message received");
	                } else {
	                    failedReason = "message received was not a TextMessage";
	                }
	                consumer.close();
	                session.close();
	                connection.close();
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
		oneShotReceiver.join();
		assertNull(oneShotReceiver.failedReason, oneShotReceiver.failedReason);
		assertNull(oneShotReceiver.failedReason, oneShotSender.failedReason);
		assertEquals(messageToSend, oneShotReceiver.messageReceived);
	}
	

	public void testCanSendAndReceiveFromJmsQueueUsingMessageListener() throws InterruptedException, JMSException {
        final String queue = "org.essentialplatform.messageListener";
		final String messageToSend = UUID.randomUUID().toString();
		class OneShotSender extends Thread {
			String failedReason;
			@Override
	        public void run() {
	            try {
	                ActiveMQConnectionFactory connectionFactory = 
	                	new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
	                Connection connection = connectionFactory.createConnection();
	                connection.start();
	                System.out.println("OneShotReceiver: connected");
	                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
					Destination destination = session.createQueue(queue);
	                MessageProducer producer = session.createProducer(destination);
	                System.out.println("OneShotReceiver: consumer obtained");
	                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	                TextMessage message = session.createTextMessage(messageToSend);
	                producer.send(message);
	                System.out.println("OneShotSender: message sent");
	                session.close();
	                connection.close();
	                System.out.println("OneShotSender: connection closed");
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
                _session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				_destination = _session.createQueue(queue);
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
		assertNull(oneShotReceiver.failedReason, oneShotReceiver.failedReason);
		assertNull(oneShotReceiver.failedReason, oneShotSender.failedReason);
		assertEquals(messageToSend, oneShotReceiver.messageReceived);
	}
}
