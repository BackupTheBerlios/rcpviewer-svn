package org.essentialplatform.runtime.shared.tests.progmodel.standard;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.essentialplatform.progmodel.standard");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestEssentialProgModelStandardRules.class);
		//$JUnit-END$
		return suite;
	}

}
