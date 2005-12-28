package org.essentialplatform.runtime.shared.tests.transaction;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.essentialplatform.transaction");

		//$JUnit-BEGIN$
		suite.addTestSuite(TestTransactionManager.class);
		suite.addTestSuite(TestTransactionManagerEnlistPojo.class);
		suite.addTestSuite(TestTransactionManagerSessionInteraction.class);
		suite.addTestSuite(TestTransactionManagerCurrentTransactions.class);
		suite.addTestSuite(TestTransactionManagerStartTransaction.class);
		suite.addTestSuite(TestTransactionManagerReverseReapply.class);
		suite.addTestSuite(TestTransactionManagerUndoRedo.class);
		suite.addTestSuite(TestTransactionManagerChangeSets.class);
		suite
				.addTestSuite(TestTransactionManagerStartTransactionInstantiate.class);
		suite.addTestSuite(TestTransactionManagerEnlistPojoInstantiate.class);
		suite.addTestSuite(TestTransactionCommitInstantiate.class);
		suite
				.addTestSuite(TestTransactionManagerNotifiesListenerInstantiate.class);
		suite.addTestSuite(TestTransactionManagerNotifiesListener.class);
		suite.addTestSuite(TestTransactionNotifiesListener.class);
		//$JUnit-END$
		return suite;
	}

}
