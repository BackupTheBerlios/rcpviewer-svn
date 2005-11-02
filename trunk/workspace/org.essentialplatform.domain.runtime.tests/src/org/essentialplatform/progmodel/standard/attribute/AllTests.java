package org.essentialplatform.progmodel.standard.attribute;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.essentialplatform.progmodel.standard.attribute");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestDomainClassAttributesAtRuntime.class);
		suite.addTestSuite(TestDomainClassAttributesUniquenessAtRuntime.class);
		suite.addTestSuite(TestDomainClassAttributesOrderingAtRuntime.class);
		suite.addTestSuite(TestDomainClassAttributesCardinalityAtRuntime.class);
		suite.addTestSuite(TestDomainClassAttributesUnsettabilityAtRuntime.class);
		//$JUnit-END$
		return suite;
	}

}
