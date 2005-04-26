package de.berlios.rcpviewer.facade;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for de.berlios.rcpviewer.facade");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestFacade.class);
		//$JUnit-END$
		return suite;
	}

}
