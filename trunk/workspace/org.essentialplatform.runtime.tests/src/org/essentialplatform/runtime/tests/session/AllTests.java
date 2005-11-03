package org.essentialplatform.runtime.tests.session;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.session");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestDomainObjectReference.class);
		suite.addTestSuite(TestDomainObjectAttribute.class);
		suite.addTestSuite(TestSessionAttachDetach.class);
		suite.addTestSuite(TestDomainObjectOperation.class);
		suite.addTestSuite(TestDomainObjectCollection.class);
		suite.addTestSuite(TestSessionManager.class);
		suite.addTestSuite(TestSessionFootprint.class);
		suite.addTestSuite(TestDomainObjectPersist.class);
		suite.addTestSuite(TestSession.class);
		suite.addTestSuite(TestExtendedDomainObjectAttribute.class);
		suite.addTestSuite(TestExtendedDomainObjectOperation.class);
		suite
				.addTestSuite(TestExtendedDomainObjectAttributeAuthorization.class);
		//$JUnit-END$
		return suite;
	}

}
