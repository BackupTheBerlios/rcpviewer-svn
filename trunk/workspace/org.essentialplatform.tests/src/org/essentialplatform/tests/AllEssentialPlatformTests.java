package org.essentialplatform.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Regression test suite for all test packages.
 * 
 * <p>
 * What I'd ideally like is for any and all unit test suites to be added to
 * this test, which should then be run as both a JUnit test and also a
 * JUnit plugin test.  Though I expect we'll need to get more sophisticated
 * than that before too long...
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
		suite.addTest(org.essentialplatform.progmodel.essential.app.tests.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
