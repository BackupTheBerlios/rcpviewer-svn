package org.essentialplatform;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform");
		//$JUnit-BEGIN$
		suite.addTest(org.essentialplatform.progmodel.standard.AllTests.suite());
		//$JUnit-END$
		return suite;
	}


}
