package org.essentialplatform.server.tests.remoting.activemq;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.server.test.remoting.activemq");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestStartAndShutdown.class);
		suite.addTestSuite(TestLocalSendReceive.class);
		suite.addTestSuite(TestSendTransactionsUsingXStream.class);
		//$JUnit-END$
		return suite;
	}

}
