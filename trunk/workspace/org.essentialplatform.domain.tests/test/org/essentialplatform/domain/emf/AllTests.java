package org.essentialplatform.domain.emf;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.domain");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestEmfAnnotations.class);
		suite.addTestSuite(TestEmf.class);
		//$JUnit-END$
		return suite;
	}

}
