package de.berlios.rcpviewer.metamodel;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.metamodel");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestMetaModel.class);
		suite.addTestSuite(TestEmfFacade.class);
		suite.addTestSuite(TestDomainObject.class);
		//$JUnit-END$
		return suite;
	}

}
