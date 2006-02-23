package org.essentialplatform.runtime.server.tests.session.hibernate;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.runtime.server.tests.session.hibernate");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestHibernateServerSession.class);
		//$JUnit-END$
		return suite;
	}

}
