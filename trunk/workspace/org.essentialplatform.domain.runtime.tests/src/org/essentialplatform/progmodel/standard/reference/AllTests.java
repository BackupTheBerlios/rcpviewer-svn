package org.essentialplatform.progmodel.standard.reference;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.essentialplatform.progmodel.standard.reference");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestDomainClassReferencesAtRuntime.class);
		//$JUnit-END$
		return suite;
	}

}
