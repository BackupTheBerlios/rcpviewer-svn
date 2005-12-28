package org.essentialplatform.server.tests.remoting.activemq;

import junit.framework.TestCase;

import org.essentialplatform.server.remoting.activemq.ActiveMqRemotingServer;

public class TestStartAndShutdown extends TestCase {

	protected void setUp() throws Exception {
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
