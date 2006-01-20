/**
 * 
 */
package org.essentialplatform.runtime.server.remoting.activemq;

import java.io.ByteArrayInputStream;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.ITransactionProcessor;
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.transaction.ITransaction;

class TransactionMessageListener extends Thread implements ExceptionListener, MessageListener {
	
	private Logger getLogger() {
		return Logger.getLogger(TransactionMessageListener.class);
	}

	private final ActiveMqRemotingServer _remotingServer;
	
	private final ActiveMQConnectionFactory _connectionFactory;
	private final Connection _connection;
	private final Session _session;
	private final Destination _destination;
	private final MessageConsumer _consumer;

	TransactionMessageListener(final ActiveMqRemotingServer remotingServer) 
			throws JMSException {
		_remotingServer = remotingServer;
		
        _connectionFactory = new ActiveMQConnectionFactory(_remotingServer.getBindAddress());
		_connection = _connectionFactory.createConnection();
		_connection.start();
        _connection.setExceptionListener(this);
        _session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		_destination = _session.createQueue(_remotingServer.getQueue());
		_consumer = _session.createConsumer(_destination);
		installListener();
	}

	public void onException(JMSException ex) {
		getLogger().error("JMSException - continuing", ex);
	}

	public void onMessage(Message message) {
		try {
	        if (!(message instanceof TextMessage)) {
	            getLogger().warn("Unknown message type '" + message.getClass().getName() + "' received; discarding");
	            return;
	        }
            TextMessage textMessage = (TextMessage) message;
            String messageReceived = textMessage.getText();
            getLogger().debug("Text message received: " + messageReceived.length() + " characters");
            Object obj = _remotingServer.getMarshalling().unmarshal(messageReceived);
			
			if (!(obj instanceof ITransactionPackage)) {
				getLogger().warn("Unmarshalled message not a xactn package (instead '" + obj.getClass().getName() + "'); discarding");
			}
			ITransactionPackage packagedXactn = (ITransactionPackage)obj;
			_remotingServer.getTransactionProcessor().process(packagedXactn);
		} catch (JMSException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}


	private void installListener() throws JMSException {
		_consumer.setMessageListener(this);
	}
	
	/**
	 * Called when the {@link ActiveMqRemotingServer} shuts down.
	 *
	 */
	void uninstallListener() {
		try {
			_consumer.setMessageListener(null);
		} catch (JMSException ex) {
			getLogger().warn("Exception while unregistering - continuing", ex);
		}
	}
}