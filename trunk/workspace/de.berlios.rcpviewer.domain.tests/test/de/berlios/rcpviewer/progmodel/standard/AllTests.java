package de.berlios.rcpviewer.progmodel.standard;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.progmodel.standard");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestStandardProgModelRules.class);
		//$JUnit-END$
		return suite;
	}

}
