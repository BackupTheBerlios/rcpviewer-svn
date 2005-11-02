package org.essentialplatform;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Deployment-neutralTest for org.essentialplatform");
		//$JUnit-BEGIN$
		suite.addTest(org.essentialplatform.domain.emf.AllTests.suite());
		suite.addTest(org.essentialplatform.progmodel.standard.AllTests.suite()); // TODO: move to runtime.
		//$JUnit-END$
		return suite;
	}


}
