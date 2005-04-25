package de.berlios.rcpviewer.metamodel.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.metamodel.impl");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestDomainAspect.class);
		suite.addTestSuite(TestRegisterDomainClassAspect.class);
		//$JUnit-END$
		return suite;
	}

}
