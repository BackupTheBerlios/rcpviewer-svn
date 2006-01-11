package org.essentialplatform.runtime.tests.remoting;


import junit.framework.Test;
import junit.framework.TestSuite;

public class AllRemotingTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.remoting");
		//$JUnit-BEGIN$
		suite.addTest(org.essentialplatform.runtime.tests.remoting.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
