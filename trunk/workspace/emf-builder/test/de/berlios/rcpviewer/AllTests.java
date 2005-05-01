package de.berlios.rcpviewer;

import de.berlios.rcpviewer.metamodel.TestDomainClassRegistry;
import de.berlios.rcpviewer.metamodel.TestEmfFacade;
import de.berlios.rcpviewer.progmodel.standard.TestDomainClass;
import de.berlios.rcpviewer.progmodel.standard.TestProgrammingModel;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for de.berlios.rcpviewer");
		//$JUnit-BEGIN$
		suite.addTest(de.berlios.rcpviewer.metamodel.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.progmodel.standard.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.progmodel.standard.impl.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.session.AllTests.suite());
		//$JUnit-END$
		return suite;
	}


}
