package de.berlios.rcpviewer.progmodel.extended.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.progmodel.extended.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestPrerequisites.class);
		//$JUnit-END$
		return suite;
	}

}
