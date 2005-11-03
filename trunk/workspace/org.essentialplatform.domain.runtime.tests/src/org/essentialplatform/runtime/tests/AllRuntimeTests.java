package org.essentialplatform.runtime.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllRuntimeTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform");
		//$JUnit-BEGIN$
		suite.addTest(org.essentialplatform.runtime.tests.persistence.AllTests.suite());

		suite.addTest(org.essentialplatform.runtime.tests.progmodel.standard.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.tests.progmodel.standard.attribute.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.tests.progmodel.standard.domainclass.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.tests.progmodel.standard.i18n.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.tests.progmodel.standard.namesanddesc.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.tests.progmodel.standard.operation.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.tests.progmodel.standard.reference.AllTests.suite());
		
		suite.addTest(org.essentialplatform.runtime.tests.progmodel.extended.AllTests.suite());
		
		suite.addTest(org.essentialplatform.runtime.tests.progmodel.rcpviewer.AllTests.suite());

		suite.addTest(org.essentialplatform.runtime.tests.session.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.tests.transaction.AllTests.suite());
		
		//$JUnit-END$
		return suite;
	}

}
