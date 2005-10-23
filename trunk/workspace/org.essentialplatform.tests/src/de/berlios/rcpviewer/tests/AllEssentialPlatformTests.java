package de.berlios.rcpviewer.tests;

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
		TestSuite suite = new TestSuite("Test for de.berlios.rcpviewer.tests");
		//$JUnit-BEGIN$
		suite.addTest(de.berlios.rcpviewer.AllTests.suite()); // generic
		suite.addTest(de.berlios.rcpviewer.AllRuntimeTests.suite());
		suite.addTest(de.berlios.rcpviewer.progmodel.extended.tests.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
