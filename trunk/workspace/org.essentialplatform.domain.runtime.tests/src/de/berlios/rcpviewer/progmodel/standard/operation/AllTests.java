package de.berlios.rcpviewer.progmodel.standard.operation;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.progmodel.standard.operation");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestDomainClassOperationsAtRuntime.class);
		//$JUnit-END$
		return suite;
	}

}
