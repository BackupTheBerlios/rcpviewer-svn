package org.essentialplatform.server.tests;


import junit.framework.Test;
import junit.framework.TestSuite;

public class AllServerTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.server");
		//$JUnit-BEGIN$
		suite.addTest(org.essentialplatform.server.tests.persistence.AllTests.suite());
		suite.addTest(org.essentialplatform.server.tests.persistence.inmemory.AllTests.suite());
		suite.addTest(org.essentialplatform.server.tests.database.hsqldb.AllTests.suite());
		suite.addTest(org.essentialplatform.server.tests.hibernate.AllTests.suite());
		suite.addTest(org.essentialplatform.server.tests.remoting.activemq.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
