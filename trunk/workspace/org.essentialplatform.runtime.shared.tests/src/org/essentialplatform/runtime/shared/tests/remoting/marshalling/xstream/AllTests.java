package org.essentialplatform.runtime.shared.tests.remoting.marshalling.xstream;


import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.runtime.shared.remoting.marshalling.xstream");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestXStreamMarshalling.class);
		//$JUnit-END$
		return suite;
	}

}
