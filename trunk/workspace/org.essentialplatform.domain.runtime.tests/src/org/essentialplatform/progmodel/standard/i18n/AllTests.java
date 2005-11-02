package org.essentialplatform.progmodel.standard.i18n;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.essentialplatform.progmodel.standard.i18n");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestInternationalizationAtRuntime.class);
		//$JUnit-END$
		return suite;
	}

}
