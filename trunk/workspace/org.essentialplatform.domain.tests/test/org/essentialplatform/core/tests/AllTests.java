package org.essentialplatform.core.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Deployment-neutralTest for org.essentialplatform");
		//$JUnit-BEGIN$
		suite.addTest(org.essentialplatform.core.tests.emf.AllTests.suite());
		//$JUnit-END$
		return suite;
	}


}
