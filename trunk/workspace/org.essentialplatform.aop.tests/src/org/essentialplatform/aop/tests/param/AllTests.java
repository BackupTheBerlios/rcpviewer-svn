package org.essentialplatform.aop.tests.param;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.aop.param");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestMandatory.class);
		//$JUnit-END$
		return suite;
	}

}
