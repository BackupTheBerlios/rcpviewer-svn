package org.essentialplatform.progmodel.rcpviewer;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.essentialplatform.progmodel.rcpviewer");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestImageDescriptorAtRuntime.class);
		//$JUnit-END$
		return suite;
	}

}
