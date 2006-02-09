package org.essentialplatform.runtime.server.remoting.activemq;

import javax.jms.JMSException;

import org.activemq.ActiveMQConnection;
import org.apache.log4j.Logger;
import org.essentialplatform.runtime.server.AbstractServer;
import org.essentialplatform.runtime.server.remoting.IRemotingServer;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.ITransactionProcessor;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.noop.NoopTransactionProcessor;
import org.essentialplatform.runtime.shared.remoting.activemq.ActiveMqServerConstants;
import org.essentialplatform.runtime.shared.remoting.activemq.DestinationCleaner;
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.marshalling.xstream.XStreamMarshalling;

public class ActiveMqRemotingServer extends AbstractServer implements IRemotingServer {

	private static final long DESTINATION_CLEANER_TIMEOUT = 200L;
	private static final boolean TRANSACTED = true;

	@Override
	protected Logger getLogger() {
		return Logger.getLogger(ActiveMqRemotingServer.class);
	}

	
    ////////////////////////////////////////////////////////////
	// Constructor, dependenciesInjected
    ////////////////////////////////////////////////////////////
	
	/**
	 * The system property <tt>activemq.store.dir</tt> is set to the value of
	 * <tt>java.io.tmpdir</tt>.
	 */
	public ActiveMqRemotingServer() {
		System.setProperty("activemq.store.dir", System.getProperty("java.io.tmpdir"));
	}

	public void dependenciesInjected() {
		if (_destinationCleaner != null) {
			_destinationCleaner.setBrokerUrl(getBindAddress());
			_destinationCleaner.setDestinationName(getQueue());
			_destinationCleaner.setMessageReceiveTimeout(DESTINATION_CLEANER_TIMEOUT);
			_destinationCleaner.setTransacted(TRANSACTED);
		}
		
	}
	
    ////////////////////////////////////////////////////////////
	// Lifecycle Methods
	// start, shutdown
    ////////////////////////////////////////////////////////////

	/**
	 * Non null iff the broker is running.
	 */
	private Thread brokerThread;

    /**
     * Will be populated iff <tt>_startListener</tt> was set when the server
     * itself was started.
     */
	private TransactionMessageListener _messageListener;

    @Override
	public boolean doStart() {
		ActiveMqBrokerWrapper brokerWrapper;
		try {
			brokerWrapper = new ActiveMqBrokerWrapper(this);
		} catch (JMSException ex) {
			getLogger().error("ActiveMqRemotingServer: broker failed to start", ex);
			return false;
		}
		brokerThread = brokerWrapper.start();
		
		getLogger().info("ActiveMqRemotingServer: broker started");
		
		if (_destinationCleaner != null) {
			getLogger().debug("Cleaning destination " + _destinationCleaner.getDestinationName());
			_destinationCleaner.clean();
		}
	
		if (_startListener) {
			try {
				// marshalling mechanism is propagated to the message listener.
				_messageListener = new TransactionMessageListener(this);
		        getLogger().debug("message listener started");
				return true;
			} catch (JMSException ex) {
				return false;
			}
		}
		return true;
	}


    @Override
	public boolean doShutdown() {
		if (brokerThread == null) {
			// should never happen.
			throw new IllegalStateException("Shutdown requested (isStarted() = true), but brokerThread is null");
		}
		
		if (_messageListener != null) {
			_messageListener.uninstallListener();
		}
		
		brokerThread.interrupt();
		
		// wait for the broker thread to finish.
		boolean rejoined = false;
		int i=0;
		while(!rejoined) {
			try {
				brokerThread.join(1000);
			} catch (InterruptedException ex) {
				getLogger().warn("Ignoring interrupt - waiting for brokerThread to terminate");
			}
			rejoined = !brokerThread.isAlive();
			if (!rejoined) {
				++i;
				getLogger().info(String.format("Waiting for broker to quit (%d seconds)", i));
			}
		}
		return true;
	}


    ////////////////////////////////////////////////////////////
    // Queue (injected)
    ////////////////////////////////////////////////////////////

    private String _queue = ActiveMqServerConstants.TRANSACTION_QUEUE; 
	String getQueue() {
		return _queue;
	}
	/**
	 * Specify the queue to which transactions are sent.
	 * 
	 * <p>
	 * Optional; defaults to {@link ActiveMqServerConstants#TRANSACTION_QUEUE}.
	 * 
	 * @param queue
	 */
	public void setQueue(String queue) {
		_queue = queue;
	}


	
    ////////////////////////////////////////////////////////////
    // BindAddress (injected)
    ////////////////////////////////////////////////////////////
    
    private String _bindAddress = ActiveMQConnection.DEFAULT_BROKER_URL;
    public String getBindAddress() {
		return _bindAddress;
	}
    /**
     * Specify the bind address for ActiveMQ to listen upon.
     * 
     * <p>
     * Any URL format understood by ActiveMQ may be used. If not called, 
     * defaults to ActiveMQ's default broker URL, namely 
     * <tt>tcp://localhost:61616</tt>. 
     *
     * @param bindAddress
     */
    public void setBindAddress(String bindAddress) {
		_bindAddress = bindAddress;
	}

    ////////////////////////////////////////////////////////////
    // StartListenerEnabled (injected)
    ////////////////////////////////////////////////////////////

    private boolean _startListener = true;
    public boolean isStartListenerEnabled() {
		return _startListener;
	}
    /**
     * Optionally prevent the message listener from being started.
     * 
     * <p>
     * The default is to start the message listener.
     * 
     * @param startListener
     */
    public void setMessageListenerEnabled(boolean startListener) {
		_startListener = startListener;
	}


    ////////////////////////////////////////////////////////////
    // Marshalling (injected)
    ////////////////////////////////////////////////////////////

	private IMarshalling _marshalling = new XStreamMarshalling();
    public IMarshalling getMarshalling() {
		return _marshalling;
	}
    /**
     * Marshalling mechanism to use.  
     * 
     * <p>
     * Can be dependency injected (see {@link #setMarshalling(IMarshalling)}), 
     * but will default to using {@link XStreamMarshalling}.
     */
	public void setMarshalling(IMarshalling marshalling) {
		_marshalling = marshalling;
	}

    ////////////////////////////////////////////////////////////
    // TransactionProcessor (injected)
    ////////////////////////////////////////////////////////////

    private ITransactionProcessor transactionProcessor = new NoopTransactionProcessor();
    public ITransactionProcessor getTransactionProcessor() {
		return transactionProcessor;
	}
    /**
     * Optionally specify the transaction processr.
     * 
     * <p>
     * This should normally be called; the default is to install a transaction
     * processor that does nothing.
     * 
     * <p>
     * Note that even if a "real" transaction processor is installed, it will
     * only be called provided that the message listener is enabled (see
     * {@link #setMessageListenerEnabled(boolean)}.

     * @see NoopTransactionProcessor
     * @param transactionProcessor
     */
    public void init(ITransactionProcessor transactionProcessor) {
		this.transactionProcessor = transactionProcessor;
	}

    ////////////////////////////////////////////////////////////
    // DestinationCleaner (injected)
    ////////////////////////////////////////////////////////////

    private DestinationCleaner _destinationCleaner;
    public DestinationCleaner getDestinationCleaner() {
		return _destinationCleaner;
	}
    /**
     * For depedency injection.
     * 
     * <p>
     * Optional; if specified then will be auto-configured with same queue
     * etc as the server itself.
     * 
     * @param destinationCleaner
     */
    public void setDestinationCleaner(DestinationCleaner destinationCleaner) {
		_destinationCleaner = destinationCleaner;
	}


}
