package org.essentialplatform.runtime.server.remoting.activemq;

import javax.jms.JMSException;

import org.activemq.ActiveMQConnection;
import org.apache.log4j.Logger;
import org.essentialplatform.runtime.server.AbstractService;
import org.essentialplatform.runtime.server.remoting.IRemotingServer;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.ITransactionProcessor;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.noop.NoopTransactionProcessor;
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.marshalling.xstream.XStreamMarshalling;

public class ActiveMqRemotingServer extends AbstractService implements IRemotingServer {

	@Override
	protected Logger getLogger() {
		return Logger.getLogger(ActiveMqRemotingServer.class);
	}

	private Thread brokerThread;

    /**
     * Will be populated iff <tt>_startListener</tt> was set when the server
     * itself was started.
     */
	private TransactionMessageListener _messageListener;
	
    ////////////////////////////////////////////////////////////
	
	/**
	 * The system property <tt>activemq.store.dir</tt> is set to the value of
	 * <tt>java.io.tmpdir</tt>.
	 */
	public ActiveMqRemotingServer() {
		System.setProperty("activemq.store.dir", System.getProperty("java.io.tmpdir"));
	}

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
    public void setTransactionProcessor(ITransactionProcessor transactionProcessor) {
		this.transactionProcessor = transactionProcessor;
	}
    
    ////////////////////////////////////////////////////////////

	String getQueue() {
		return ActiveMqServerConstants.TRANSACTION_QUEUE;
	}

    
    @Override
	public boolean doStart() {
		ActiveMqBroker broker = new ActiveMqBroker(this);
		brokerThread = new Thread(broker);
		brokerThread.start();
        System.out.println("ActiveMqRemotingServer: broker started");
	
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
		if (_messageListener != null) {
			_messageListener.uninstallListener();
		}
		if (brokerThread != null) {
			brokerThread.interrupt();
		}
		return true;
	}




}
