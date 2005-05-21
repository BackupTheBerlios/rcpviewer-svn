package de.berlios.rcpviewer.progmodel.standard;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.progmodel.standard");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestInternationalization.class);
		suite.addTestSuite(TestNamingConventions.class);
		suite.addTestSuite(TestDomainClassAttributesCardinality.class);
		suite.addTestSuite(TestDomainClass.class);
		suite.addTestSuite(TestDomainClassAttributes.class);
		suite.addTestSuite(TestDomainClassAttributesUniqueness.class);
		suite.addTestSuite(TestDomainClassOperations.class);
		suite.addTestSuite(TestDomainClassReferences.class);
		suite.addTestSuite(TestExplicitNamesAndDescriptions.class);
		suite.addTestSuite(TestDomainClassAttributesOrdering.class);
		suite.addTestSuite(TestDomainClassAttributesUnsettability.class);
		//$JUnit-END$
		return suite;
	}

}
