package org.essentialplatform.runtime.tests.remoting;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.remoting.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestXStreamMarshalling.class);
		//$JUnit-END$
		return suite;
	}

}
