package org.essentialplatform.server.tests.persistence.inmemory;

import org.essentialplatform.server.tests.persistence.inmemory.TestInMemoryObjectStore;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.essentialplatform.server.persistence");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestInMemoryObjectStore.class);
		//$JUnit-END$
		return suite;
	}

}
