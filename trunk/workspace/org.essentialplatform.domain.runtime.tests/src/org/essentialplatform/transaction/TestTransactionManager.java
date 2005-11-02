package org.essentialplatform.transaction;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.Domain;
import org.essentialplatform.persistence.IObjectStore;
import org.essentialplatform.persistence.inmemory.InMemoryObjectStore;
import org.essentialplatform.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;
import org.essentialplatform.progmodel.standard.ProgModelConstants;
import org.essentialplatform.session.IDomainObject;
import org.essentialplatform.session.ISession;
import org.essentialplatform.session.local.SessionManager;
import org.essentialplatform.transaction.internal.TransactionManager;
import junit.framework.TestCase;

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
