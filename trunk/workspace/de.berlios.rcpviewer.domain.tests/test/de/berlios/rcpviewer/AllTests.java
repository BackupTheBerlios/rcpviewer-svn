package de.berlios.rcpviewer;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for de.berlios.rcpviewer");
		//$JUnit-BEGIN$
		suite.addTest(de.berlios.rcpviewer.domain.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.progmodel.standard.namingconventions.AllTests.suite());
		//$JUnit-END$
		return suite;
	}


}
