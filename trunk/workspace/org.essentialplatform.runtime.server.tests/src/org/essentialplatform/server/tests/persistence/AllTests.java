package org.essentialplatform.server.tests.persistence;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.server.persistence");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestApplyingChangesComparator.class);
		//$JUnit-END$
		return suite;
	}

}
