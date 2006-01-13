package org.essentialplatform.runtime.shared.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllRuntimeTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform");
		//$JUnit-BEGIN$

		suite.addTest(org.essentialplatform.runtime.shared.tests.progmodel.standard.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.shared.tests.progmodel.standard.attribute.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.shared.tests.progmodel.standard.domainclass.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.shared.tests.progmodel.standard.i18n.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.shared.tests.progmodel.standard.namesanddesc.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.shared.tests.progmodel.standard.operation.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.shared.tests.progmodel.standard.reference.AllTests.suite());
		
		suite.addTest(org.essentialplatform.runtime.shared.tests.progmodel.extended.AllTests.suite());
		
		suite.addTest(org.essentialplatform.runtime.shared.tests.progmodel.rcpviewer.AllTests.suite());

		suite.addTest(org.essentialplatform.runtime.shared.tests.session.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.shared.tests.transaction.AllTests.suite());
		suite.addTest(org.essentialplatform.runtime.shared.tests.persistence.AllTests.suite());
		
		//$JUnit-END$
		return suite;
	}

}
