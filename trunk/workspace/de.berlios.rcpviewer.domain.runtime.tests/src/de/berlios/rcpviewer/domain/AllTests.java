package de.berlios.rcpviewer.domain;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for de.berlios.rcpviewer.domain");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestRuntimeDomain.class);
		suite.addTestSuite(TestRuntimeDomainClass.class);
		//$JUnit-END$
		return suite;
	}

}
