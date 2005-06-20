package de.berlios.rcpviewer.session;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for de.berlios.rcpviewer.session");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestDomainObjectPersist.class);
		suite.addTestSuite(TestSession.class);
		suite.addTestSuite(TestSessionManager.class);
		suite.addTestSuite(TestDomainObjectReference.class);
		suite.addTestSuite(TestDomainObjectOperation.class);
		suite.addTestSuite(TestDomainObjectAttribute.class);
		suite.addTestSuite(TestSessionAttachDetach.class);
		suite.addTestSuite(TestSessionFootprint.class);
		suite.addTestSuite(TestDomainObjectCollection.class);
		//$JUnit-END$
		return suite;
	}

}
