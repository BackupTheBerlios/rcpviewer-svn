package org.essentialplatform.runtime.shared.tests.remoting;


import org.essentialplatform.runtime.shared.tests.remoting.marshalling.xstream.AllTests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllRemotingTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.remoting");
		//$JUnit-BEGIN$
		suite.addTest(org.essentialplatform.runtime.shared.tests.remoting.marshalling.xstream.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
