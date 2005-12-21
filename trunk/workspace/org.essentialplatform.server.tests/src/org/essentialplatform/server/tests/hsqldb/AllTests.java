package org.essentialplatform.server.tests.hsqldb;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.server.test,hsqldb");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestHibernateClassLoading.class);
		suite.addTestSuite(TestHsqldb.class);
		//$JUnit-END$
		return suite;
	}

}
