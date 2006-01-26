package org.essentialplatform.runtime.server.remoting.activemq;

import java.io.IOException;

import javax.jms.JMSException;

import org.activemq.broker.BrokerContainer;
import org.activemq.broker.impl.BrokerContainerImpl;
import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.util.SleepUtil;

/**
 * Instantiates an ActiveMQ <tt>BrokerContainer</tt> and configures to
 * listen (using <tt>container.addConnector</tt>) to supplied bind address.
 * 
 * <p>
 * In the future, we could possibly extend this by additionally setting up
 * network connectors (using <tt>container.addNetworkConnector(_networkUri)</tt>).
 * I <i>think</i> this would yield better scalability?
 */
class ActiveMqBrokerWrapper implements Runnable {
	
	protected Logger getLogger() {
		return Logger.getLogger(ActiveMqRemotingServer.class);
	}

	private final ActiveMqRemotingServer _remotingServer;
    private final BrokerContainerImpl container;

	ActiveMqBrokerWrapper(ActiveMqRemotingServer remotingServer) throws JMSException {
		_remotingServer = remotingServer;
        container = new BrokerContainerImpl();
        container.addConnector(_remotingServer.getBindAddress());
	}
	
	public Thread start() {
		try {
			container.start();
			Thread brokerThread = new Thread(this);
			brokerThread.start();
			// a bit of a hack, but there's no way (it seems) to check that the
			// broker is up.
			SleepUtil.sleepUninterrupted(1000);
			return brokerThread;
		} catch (JMSException ex) {
            Exception linkedEx = ex.getLinkedException();
            if (linkedEx != null) {
            	//if (linkedEx instanceof org.activemq.)
            	getLogger().error("Failed to start BrokerContainer: JMSException (linked exception)", linkedEx);
             } else {
            	getLogger().error("Failed to start BrokerContainer: JMSException (no linked exception)", ex);
            }
			return null;
		}
	}
    
    public void run() {
        // wait until we're interrupted
    	try {
            Object lock = new Object();
            synchronized (lock) {
            	lock.wait();
            }
    	} catch(InterruptedException ex) {
    		// all done
        } finally {
        	stopSafely(container);
        }
    }

    private void stopSafely(BrokerContainer container) {
    	if (container != null) {
    		try {
				container.stop();
			} catch (JMSException ex) {
				// do nothing
			}
    	}
    }
}