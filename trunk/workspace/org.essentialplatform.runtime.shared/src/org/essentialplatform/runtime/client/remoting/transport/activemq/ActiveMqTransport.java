package org.essentialplatform.runtime.client.remoting.transport.activemq;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.activemq.ActiveMQConnection;
import org.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.essentialplatform.runtime.client.remoting.packaging.IPackager;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.shared.remoting.activemq.ActiveMqServerConstants;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.remoting.transport.AbstractTransport;

public final class ActiveMqTransport extends AbstractTransport {

	public Logger getLogger() { return Logger.getLogger(ActiveMqTransport.class); }
	
	private ActiveMQConnectionFactory _connectionFactory;
	private Connection _connection;
	private Session _session;
	private MessageProducer _producer;

	
	/*
	 * @see org.essentialplatform.runtime.shared.remoting.transport.ITransport#start()
	 */
	public void start() {
		assertNotStarted();
        _connectionFactory = new ActiveMQConnectionFactory(getBrokerUrl());
		try {
			_connection = _connectionFactory.createConnection();
			_connection.start();
	        _session = _connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			_destination = _session.createQueue(_destinationName);
			_producer = _session.createProducer(_destination);
			_producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			_started = true;
		} catch (JMSException ex) {
			getLogger().error("Could not start");
			throw new RuntimeException("Unable to start", ex);
		}
	}

	/*
	 * @see org.essentialplatform.runtime.shared.remoting.transport.ITransport#shutdown()
	 */
	public void shutdown() {
		assertStarted();
        try {
        	_producer.close();
			_session.close();
	        _connection.close();
			_started = false;
		} catch (JMSException ex) {
			getLogger().error("Could not stop");
			throw new RuntimeException("Unable to stop", ex);
		}
	}


	public void send(Serializable serializableMessage) {
        ObjectMessage message;
		try {
			message = _session.createObjectMessage(serializableMessage);
	        _producer.send(message);
	        _session.commit();
		} catch (JMSException ex) {
			ex.printStackTrace();
		}
	}
	
	public void send(String stringMessage) {
		assertStarted();
        TextMessage message;
		try {
			message = _session.createTextMessage(stringMessage);
	        _producer.send(message);
	        _session.commit();
		} catch (JMSException ex) {
			ex.printStackTrace();
		}
	}


	///////////////////////////////////////////////////////////////////
	// destination, destinationName
	///////////////////////////////////////////////////////////////////
	
	private Destination _destination;
	
	private String _destinationName = ActiveMqServerConstants.TRANSACTION_QUEUE;
	public String getDestinationName() {
		return _destinationName;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified, then defaults to 
	 * {@link ActiveMqServerConstants#TRANSACTION_QUEUE}.
	 * 
	 * <p>
	 * Must be specified before the transport is started (see {@link #start()}).
	 * 
	 * @param destinationName
	 */
	public void setDestinationName(String destinationName) {
		_destinationName = destinationName;
	}


	///////////////////////////////////////////////////////////////////
	// brokerUrl
	///////////////////////////////////////////////////////////////////
	
	private String _brokerUrl = ActiveMQConnection.DEFAULT_BROKER_URL;
	public String getBrokerUrl() {
		return _brokerUrl;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified, then defaults to the ActiveMQ default broker
	 * URL (<tt>tcp://localhost:61616</tt>).
	 * 
	 * @param brokerUrl
	 */
	public void setBrokerUrl(String brokerUrl) {
		_brokerUrl = brokerUrl;
	}
	

	///////////////////////////////////////////////////////////////////
	// state (started or not)
	///////////////////////////////////////////////////////////////////
	
	private boolean _started;
	
	private void assertNotStarted() {
		if (_started) {
			throw new IllegalStateException("Already started.");
		}
	}
	private void assertStarted() {
		if (!_started) {
			throw new IllegalStateException("Not yet started.");
		}
	}

	
}
