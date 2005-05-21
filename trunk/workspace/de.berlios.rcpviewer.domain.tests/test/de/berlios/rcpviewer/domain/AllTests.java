package de.berlios.rcpviewer.domain;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.domain");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestDomainClass.class);
		suite.addTestSuite(TestEmfFacade.class);
		suite.addTestSuite(TestMetaModel.class);
		//$JUnit-END$
		return suite;
	}

}
