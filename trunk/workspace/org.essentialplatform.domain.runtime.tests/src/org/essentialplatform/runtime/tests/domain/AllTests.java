package org.essentialplatform.runtime.tests.domain;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.domain");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestRuntimeDomain.class);
		suite.addTestSuite(TestRuntimeDomainClass.class);
		//$JUnit-END$
		return suite;
	}

}
