package org.essentialplatform.runtime.shared.tests.domain.handle;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.runtime.persistence");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestHandle.class);
		suite.addTestSuite(TestSequentialHandleAssigner.class);
		suite.addTestSuite(TestGuidHandleAssigner.class);
		suite.addTestSuite(TestCompositeIdHandleAssigner.class);
		suite.addTestSuite(TestIdSemanticsHandleAssigner.class);
		suite.addTestSuite(TestHandleMap.class);
		//$JUnit-END$
		return suite;
	}

}
