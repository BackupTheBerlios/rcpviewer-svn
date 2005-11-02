package org.essentialplatform;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllRuntimeTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform");
		//$JUnit-BEGIN$
		suite.addTest(org.essentialplatform.persistence.AllTests.suite());

		suite.addTest(org.essentialplatform.progmodel.standard.AllTests.suite());
		suite.addTest(org.essentialplatform.progmodel.standard.attribute.AllTests.suite());
		suite.addTest(org.essentialplatform.progmodel.standard.domainclass.AllTests.suite());
		suite.addTest(org.essentialplatform.progmodel.standard.i18n.AllTests.suite());
		suite.addTest(org.essentialplatform.progmodel.standard.namesanddesc.AllTests.suite());
		suite.addTest(org.essentialplatform.progmodel.standard.operation.AllTests.suite());
		suite.addTest(org.essentialplatform.progmodel.standard.reference.AllTests.suite());
		
		suite.addTest(org.essentialplatform.progmodel.extended.AllTests.suite());
		
		suite.addTest(org.essentialplatform.progmodel.rcpviewer.AllTests.suite());

		suite.addTest(org.essentialplatform.session.AllTests.suite());
		suite.addTest(org.essentialplatform.transaction.AllTests.suite());
		
		//$JUnit-END$
		return suite;
	}

}
