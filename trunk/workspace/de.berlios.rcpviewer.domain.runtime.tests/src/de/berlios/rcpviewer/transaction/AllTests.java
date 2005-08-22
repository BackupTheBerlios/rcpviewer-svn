package de.berlios.rcpviewer.transaction;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.transaction");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestTransactionManager.class);
		//$JUnit-END$
		return suite;
	}

}
