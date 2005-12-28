package org.essentialplatform.server.remoting.activemq;

import javax.jms.JMSException;

import org.activemq.broker.BrokerContainer;
import org.activemq.broker.impl.BrokerContainerImpl;
import org.apache.log4j.Logger;

/**
 * Instantiates an ActiveMQ <tt>BrokerContainer</tt> and configures to
 * listen (using <tt>container.addConnector</tt>) to supplied bind address.
 * 
 * <p>
 * In the future, we could possibly extend this by additionally setting up
 * network connectors (using <tt>container.addNetworkConnector(_networkUri)</tt>.
 * I <i>think</i> this would yield better scalability?
 */
class ActiveMqBroker implements Runnable {
	
	protected Logger getLogger() {
		return Logger.getLogger(ActiveMqRemotingServer.class);
	}

	private final ActiveMqRemotingServer _remotingServer;
	ActiveMqBroker(ActiveMqRemotingServer remotingServer) {
		_remotingServer = remotingServer;
	}
	
    public void run() {
        try {
            BrokerContainer container = new BrokerContainerImpl();
            container.addConnector(_remotingServer.getBindAddress());
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
        } catch (JMSException ex) {
            Exception linkedEx = ex.getLinkedException();
            if (linkedEx != null) {
                getLogger().error("JMSException with linked exception", linkedEx);
            } else {
            	getLogger().error("JMSException (no linked exception)", ex);
            }
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
}