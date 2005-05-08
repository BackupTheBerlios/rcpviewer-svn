package de.berlios.rcpviewer.progmodel.extended;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.progmodel.extended");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestPositionedAt.class);
		//$JUnit-END$
		return suite;
	}

}
