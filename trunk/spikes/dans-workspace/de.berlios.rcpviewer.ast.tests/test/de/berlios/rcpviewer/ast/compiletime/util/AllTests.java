package de.berlios.rcpviewer.ast.compiletime.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.ast.compiletime.util");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestIdeFacade.class);
		//$JUnit-END$
		return suite;
	}

}
