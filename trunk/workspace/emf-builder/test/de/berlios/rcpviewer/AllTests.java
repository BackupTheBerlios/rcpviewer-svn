package de.berlios.rcpviewer;

import de.berlios.rcpviewer.metamodel.TestDomainClass;
import de.berlios.rcpviewer.metamodel.TestDomainClassRegistry;
import de.berlios.rcpviewer.metamodel.TestEmfFacade;
import de.berlios.rcpviewer.metamodel.TestProgrammingModel;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for de.berlios.rcpviewer");
		//$JUnit-BEGIN$
		suite.addTest(de.berlios.rcpviewer.metamodel.AllTests.suite());
		suite.addTest(de.berlios.rcpviewer.metamodel.impl.AllTests.suite());
		//$JUnit-END$
		return suite;
	}


}
