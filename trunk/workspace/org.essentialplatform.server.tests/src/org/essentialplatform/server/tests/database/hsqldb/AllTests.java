package org.essentialplatform.server.tests.database.hsqldb;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.server.test.database.hsqldb");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestHsqlDatabaseServer.class);
		suite.addTestSuite(TestHsqlDatabaseServerStartShutdown.class);
		//$JUnit-END$
		return suite;
	}

}
