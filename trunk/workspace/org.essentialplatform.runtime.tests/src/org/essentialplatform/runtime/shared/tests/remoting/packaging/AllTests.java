package org.essentialplatform.runtime.shared.tests.remoting.packaging;


import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.runtime.shared.remoting.packaging");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestStandardPackager.class);
		//$JUnit-END$
		return suite;
	}

}
