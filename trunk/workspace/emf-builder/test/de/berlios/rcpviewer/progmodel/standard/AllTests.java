package de.berlios.rcpviewer.progmodel.standard;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.progmodel.standard");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestProgrammingModel.class);
		suite.addTestSuite(TestDomainClass.class);
		//$JUnit-END$
		return suite;
	}

}
