package de.berlios.rcpviewer.transaction;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.transaction");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestTransactionManager.class);
		suite.addTestSuite(TestTransactionManagerEnlistPojo.class);
		suite.addTestSuite(TestTransactionManagerSessionInteraction.class);
		suite.addTestSuite(TestTransactionManagerInstantiate.class);
		suite.addTestSuite(TestTransactionManagerCurrentTransactions.class);
		suite.addTestSuite(TestTransactionManagerStartTransaction.class);
		suite.addTestSuite(TestTransactionManagerUndoRedo.class);
		suite.addTestSuite(TestTransactionManagerChangeSets.class);
		suite.addTestSuite(TestTransactionManagerReverseReapply.class);
		//$JUnit-END$
		return suite;
	}

}
