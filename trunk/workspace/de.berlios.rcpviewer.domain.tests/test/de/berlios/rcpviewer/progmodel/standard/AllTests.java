package de.berlios.rcpviewer.progmodel.standard;

import de.berlios.rcpviewer.progmodel.standard.attribute.TestDomainClassAttributes;
import de.berlios.rcpviewer.progmodel.standard.attribute.TestDomainClassAttributesCardinality;
import de.berlios.rcpviewer.progmodel.standard.attribute.TestDomainClassAttributesOrdering;
import de.berlios.rcpviewer.progmodel.standard.attribute.TestDomainClassAttributesUniqueness;
import de.berlios.rcpviewer.progmodel.standard.attribute.TestDomainClassAttributesUnsettability;
import de.berlios.rcpviewer.progmodel.standard.domainclass.TestDomainClass;
import de.berlios.rcpviewer.progmodel.standard.i18n.TestInternationalization;
import de.berlios.rcpviewer.progmodel.standard.namesanddesc.TestExplicitNamesAndDescriptions;
import de.berlios.rcpviewer.progmodel.standard.namingconventions.TestNamingConventions;
import de.berlios.rcpviewer.progmodel.standard.operation.TestDomainClassOperations;
import de.berlios.rcpviewer.progmodel.standard.reference.TestDomainClassReferences;
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
