package de.berlios.rcpviewer.progmodel.rcpviewer;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.progmodel.rcpviewer");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestImageDescriptor.class);
		//$JUnit-END$
		return suite;
	}

}
