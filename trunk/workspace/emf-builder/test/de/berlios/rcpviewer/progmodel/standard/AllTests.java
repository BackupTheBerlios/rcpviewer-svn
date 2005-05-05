package de.berlios.rcpviewer.progmodel.standard;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.progmodel.standard");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestNamingConventions.class);
		suite.addTestSuite(TestDomainClass.class);
		suite.addTestSuite(TestDomainClassAttributes.class);
		suite.addTestSuite(TestDomainClassOperations.class);
		suite.addTestSuite(TestDomainClassLinks.class);
		//$JUnit-END$
		return suite;
	}

}
