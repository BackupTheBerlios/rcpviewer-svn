package org.essentialplatform.runtime.client.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.server.test.remoting.activemq");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestSendTransactionsUsingXStreamBootedViaSpring.class);
		//$JUnit-END$
		return suite;
	}

}
