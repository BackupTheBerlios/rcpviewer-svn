package org.essentialplatform.remoting.tests;


import junit.framework.Test;
import junit.framework.TestSuite;

public class AllRemotingTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.remoting");
		//$JUnit-BEGIN$
		suite.addTest(org.essentialplatform.remoting.tests.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
