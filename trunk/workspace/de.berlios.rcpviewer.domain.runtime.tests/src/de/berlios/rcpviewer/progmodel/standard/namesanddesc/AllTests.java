package de.berlios.rcpviewer.progmodel.standard.namesanddesc;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.progmodel.standard.namesanddesc");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestExplicitNamesAndDescriptionsAtRuntime.class);
		//$JUnit-END$
		return suite;
	}

}
