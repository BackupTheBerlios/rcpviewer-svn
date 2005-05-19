package de.berlios.rcpviewer.progmodel.standard.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.domain.impl");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestDomainAspect.class);
		suite.addTestSuite(TestRegisterDomainClassAspect.class);
		//$JUnit-END$
		return suite;
	}

}
