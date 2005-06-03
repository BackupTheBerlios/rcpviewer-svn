package de.berlios.rcpviewer;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllRuntimeTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for de.berlios.rcpviewer");
		//$JUnit-BEGIN$
		suite.addTest(de.berlios.rcpviewer.domain.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.progmodel.extended.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.progmodel.rcpviewer.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.progmodel.standard.attribute.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.progmodel.standard.domainclass.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.progmodel.standard.i18n.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.progmodel.standard.namesanddesc.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.progmodel.standard.operation.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.progmodel.standard.reference.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.session.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
