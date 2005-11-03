package org.essentialplatform.runtime.tests.progmodel.standard.domainclass;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.essentialplatform.progmodel.standard.domainclass");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestDomainClassImmutableAtRuntime.class);
		suite.addTestSuite(TestDomainClassInstantiableAtRuntime.class);
		suite.addTestSuite(TestDomainClassSearchableAtRuntime.class);
		suite.addTestSuite(TestDomainClassSaveableAtRuntime.class);
		suite.addTestSuite(TestDomainClassAtRuntime.class);
		//$JUnit-END$
		return suite;
	}

}
