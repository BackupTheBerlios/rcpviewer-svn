package org.essentialplatform.server.tests.remoting.activemq;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.essentialplatform.runtime.server.remoting.activemq.ActiveMqRemotingServer;

public class TestStartAndShutdown extends TestCase {

	protected void setUp() throws Exception {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testStartAndShutdown() {
		ActiveMqRemotingServer remotingServer;
		remotingServer = new ActiveMqRemotingServer();
		remotingServer.setMessageListenerEnabled(false);
		remotingServer.start();
		remotingServer.shutdown();
	}


}
