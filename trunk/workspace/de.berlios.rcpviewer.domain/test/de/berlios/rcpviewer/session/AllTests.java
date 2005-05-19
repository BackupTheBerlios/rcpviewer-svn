package de.berlios.rcpviewer.session;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for de.berlios.rcpviewer.session");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestDomainObject.class);
		suite.addTestSuite(TestSession.class);
		//$JUnit-END$
		return suite;
	}

}
