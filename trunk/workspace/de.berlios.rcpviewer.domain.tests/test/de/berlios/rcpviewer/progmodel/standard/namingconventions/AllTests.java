package de.berlios.rcpviewer.progmodel.standard.namingconventions;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.progmodel.standard.namingconventions");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestNamingConventions.class);
		//$JUnit-END$
		return suite;
	}

}
