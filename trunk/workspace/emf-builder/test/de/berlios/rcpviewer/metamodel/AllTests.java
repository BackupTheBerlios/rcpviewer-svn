package de.berlios.rcpviewer.metamodel;

import de.berlios.rcpviewer.progmodel.standard.TestDomainClass;
import de.berlios.rcpviewer.progmodel.standard.TestProgrammingModel;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.metamodel");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestProgrammingModel.class);
		suite.addTestSuite(TestDomainClassRegistry.class);
		suite.addTestSuite(TestDomainClass.class);
		suite.addTestSuite(TestEmfFacade.class);
		//$JUnit-END$
		return suite;
	}

}
