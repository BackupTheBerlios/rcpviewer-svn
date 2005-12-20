package org.essentialplatform.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Regression test suite for all test packages.
 * 
 * <p>
 * Any and all unit test suites to be added to this test, which should then be 
 * run as both a JUnit test and also a JUnit plugin test.
 * 
 * @author Dan Haywood
 */
public class AllEssentialPlatformTests {

	// TODO: add Compile-time tests etc.
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.tests");
		//$JUnit-BEGIN$
		suite.addTest(org.essentialplatform.core.tests.AllTests.suite()); // deployment-neutral
		suite.addTest(org.essentialplatform.runtime.tests.AllRuntimeTests.suite()); // specific to runtime
		suite.addTest(org.essentialplatform.remoting.tests.AllRemotingTests.suite()); 
		suite.addTest(org.essentialplatform.server.tests.AllServerTests.suite()); 
		suite.addTest(org.essentialplatform.progmodel.essential.app.tests.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
