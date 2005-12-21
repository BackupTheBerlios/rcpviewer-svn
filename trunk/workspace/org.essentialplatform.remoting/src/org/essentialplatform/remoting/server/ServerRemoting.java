package org.essentialplatform.remoting.server;

import javax.jms.JMSException;

import org.activemq.ActiveMQConnection;
import org.activemq.broker.BrokerContainer;
import org.activemq.broker.impl.BrokerContainerImpl;
import org.apache.log4j.Logger;
import org.essentialplatform.remoting.AbstractRemoting;
import org.essentialplatform.remoting.marshalling.xstream.XStreamMarshalling;
import org.essentialplatform.remoting.server.transport.ActiveMqTransport;

public final class ServerRemoting extends AbstractRemoting {

	protected Logger getLogger() {
		return Logger.getLogger(ServerRemoting.class);
	}

	private Thread _transportThread;
	

	/**
	 * Defaults to using XStream marshalling with ActiveMQ transport.
	 *
	 */
	public ServerRemoting() {
		setMarshalling(new XStreamMarshalling());
		setTransport(new ActiveMqTransport());
		getTransport().setMarshalling(getMarshalling());
	}


	/*
	 * @see org.essentialplatform.remoting.IRemoting#start()
	 */
	public void start() {
		ActiveMqBroker broker = new ActiveMqBroker();
		_transportThread = new Thread(broker);
		_transportThread.start();
	}
	
	/*
	 * @see org.essentialplatform.remoting.IRemoting#stop()
	 */
	public void stop() {
		if (_transportThread != null) {
			_transportThread.interrupt();
		}
	}
	

    private static class ActiveMqBroker implements Runnable {
        private final String _bindAddress;
        private final String _networkUri; 

        public ActiveMqBroker() {
        	this(ActiveMQConnection.DEFAULT_BROKER_URL);
        }

        public ActiveMqBroker(final String bindAddress) {
            this(bindAddress, null);
        }
        public ActiveMqBroker(final String bindAddress, final String networkUri) {
            _bindAddress = bindAddress;
            _networkUri = networkUri;
        }

        public void run() {
            try {
                BrokerContainer container = new BrokerContainerImpl();
                container.addConnector(_bindAddress);
                if (_networkUri != null) {
                    container.addNetworkConnector(_networkUri);
                }
                container.start();

                // wait until we're interrupted
                Object lock = new Object();
                synchronized (lock) {
                	try {
                		lock.wait();
                	} catch(InterruptedException ex) {
                		// all done
                	}
                }
            } catch (JMSException e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
                Exception le = e.getLinkedException();
                System.out.println("Reason: " + le);
                if (le != null) {
                    le.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }
    }
}
