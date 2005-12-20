package org.essentialplatform.runtime.tests.persistence;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.runtime.persistence");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestPersistenceId.class);
		suite.addTestSuite(TestSequentialPersistenceIdAssigner.class);
		suite.addTestSuite(TestCompositeIdPersistenceIdAssigner.class);
		suite.addTestSuite(TestIdSemanticsPersistenceIdAssigner.class);
		//$JUnit-END$
		return suite;
	}

}
