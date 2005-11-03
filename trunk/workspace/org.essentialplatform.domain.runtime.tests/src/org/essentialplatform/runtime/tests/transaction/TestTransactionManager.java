package org.essentialplatform.runtime.tests.transaction;

import org.essentialplatform.runtime.transaction.ITransaction;


public class TestTransactionManager extends AbstractTransactionManagerTestCase {

	public TestTransactionManager() {
		super(); // setup objects, please.
	}


	// Commit
	public void testCommitTransactionChangesStateToCommitted() {
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		calculator.add(5);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator);
		
		assertNotNull(transaction);
		assertSame(ITransaction.State.IN_PROGRESS, transaction.getState());
		transaction.commit();
		assertSame(ITransaction.State.COMMITTED, transaction.getState());
	}


	// Reverse

	
	// Re-apply

	
	// auto-persist 
	public void incompletetestCannotAssociateNonPersistedObjectWithPersistedIfNotAutoPersist() {
		
	}

	public void incompletetestAssociatingNonPersistedObjectWithPersistedWillPersistIfAutoPersist() {
		
	}



	// Detached pojos
	
	public void incompletetestCannotEnrolPojoThatIsDetachedFromSession() {
		
	}
	
	public void incompletetestCannotEnrolPojoThatIsAttachedToDifferentSessionToThatOfTransaction() {
		
	}
	
	public void incompletetestCannotDetachPojoThatIsEnrolledInATransaction() {
		
	}
	

}
