package org.essentialplatform.runtime.tests.distribution;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.runtime.distribution");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestXStreamDistribution.class);
		//$JUnit-END$
		return suite;
	}

}
