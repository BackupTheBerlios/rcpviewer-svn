package org.essentialplatform.runtime.shared.remoting.activemq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.activemq.ActiveMQConnection;
import org.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.util.JmsUtil;
import org.essentialplatform.runtime.shared.util.SleepUtil;

/**
 * Cleans up all messages on a destination.
 * 
 * 
 * @author Dan Haywood
 */
public final class DestinationCleaner implements ExceptionListener {
	
	private static Logger LOG = Logger.getLogger(DestinationCleaner.class);
	public Logger getLogger() {
		return LOG;
	}
	
	public DestinationCleaner() {
	}
	
	private Exception _exception;
	public Exception getException() {
		return _exception;
	}
	
	private ActiveMQConnectionFactory _connectionFactory;
	private Connection _connection;
	private Session _session;
	private Destination _destination;
	private MessageConsumer _consumer;
	
	/**
	 * Removes all messages from a destination.
	 *
	 * <p>
	 * The destination must be specified before invoking, using 
	 * {@link #setDestinationName(String)}.
	 */
    public void clean() {

		getLogger().debug(String.format("%21s = '%s'", "brokerUrl", _brokerUrl));
		getLogger().debug(String.format("%21s = '%s'", "destinationName", _destinationName));
		getLogger().debug(String.format("%21s = '%b'", "transacted", _transacted));
		getLogger().debug(String.format("%21s = '%d'", "messageReceiveTimeout", _messageReceiveTimeout));
		
		if (_destinationName == null) {
			throw new IllegalStateException("Queue must be specified.");
		}
		
        _connectionFactory = new ActiveMQConnectionFactory(_brokerUrl);
		try {
        	getLogger().info("Creating session");
			_connection = _connectionFactory.createConnection();
			_connection.start();
            _connection.setExceptionListener(this);
            _session = _connection.createSession(_transacted, Session.AUTO_ACKNOWLEDGE);
			_destination = _session.createQueue(_destinationName);
			_consumer = _session.createConsumer(_destination);
			getLogger().debug("Connected to destination");
        	getLogger().info("Removing any messages ...");
        	int i = 0; // for logging only
			while(_consumer.receive(_messageReceiveTimeout) != null) {
				getLogger().debug("... removing message " + ++i);
			}
        	getLogger().info( i + " messages removed.");
        	getLogger().info("Closing");
            _session.commit();
            _consumer.close();
            _session.close();
            _connection.close();
        	getLogger().info("Done");
        } catch (Exception ex) {
        	getLogger().error("Failed", ex);
            _exception = ex;
        } finally {
        	JmsUtil.closeQuietly(_consumer);
        	JmsUtil.closeQuietly(_session);
        	JmsUtil.closeQuietly(_connection);
        }
    }
    /**
     * Any exception that occurred during the last run of {@link #clean()}.
     */
	public void onException(JMSException ex) {
		_exception = ex;
	}

	///////////////////////////////////////////////////////////////////
	// BrokerUrl
	///////////////////////////////////////////////////////////////////
	
	private String _brokerUrl = ActiveMQConnection.DEFAULT_BROKER_URL;
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified then defaults to 
	 * <tt>ActiveMQConnection.DEFAULT_BROKER_URL</tt>.
	 * 
	 * @param messageReceiveTimeout
	 */
	public String getBrokerUrl() {
		return _brokerUrl;
	}
	public DestinationCleaner setBrokerUrl(String brokerUrl) {
		_brokerUrl = brokerUrl;
		return this;
	}


	///////////////////////////////////////////////////////////////////
	// DestinationName
	///////////////////////////////////////////////////////////////////
	private String _destinationName;
	public String getDestinationName() {
		return _destinationName;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Mandatory - there is no default.
	 * 
	 * @param destination
	 */
	public DestinationCleaner setDestinationName(String destination) {
		_destinationName = destination;
		return this;
	}
	
	///////////////////////////////////////////////////////////////////
	// Transacted
	///////////////////////////////////////////////////////////////////
	
	private boolean _transacted = true;
	/**
	 * Whether the session used to clean the messages was started in
	 * transacted mode.
	 * 
	 * <p>
	 * If transacted, then any messages received will be removed by
	 * committing the session.  If not transacted, then the messages will be 
	 * automatically acknowledged.  (After all, the point of this object is to 
	 * remove the messages from the destination).
	 * 
	 * @return
	 */
	public boolean isTransacted() {
		return _transacted;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified, then defaults to <tt>true</tt>.
	 * @param transacted
	 */
	public DestinationCleaner setTransacted(boolean transacted) {
		_transacted = transacted;
		return this;
	}

	///////////////////////////////////////////////////////////////////
	// MessageReceiveTimeout
	///////////////////////////////////////////////////////////////////
	
	private long _messageReceiveTimeout = 200;
	public long getMessageReceiveTimeout() {
		return _messageReceiveTimeout;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified then defaults to <tt>200</tt> (milliseconds).
	 * 
	 * @param messageReceiveTimeout
	 */
	public DestinationCleaner setMessageReceiveTimeout(long messageReceiveTimeout) {
		_messageReceiveTimeout = messageReceiveTimeout;
		return this;
	}


}