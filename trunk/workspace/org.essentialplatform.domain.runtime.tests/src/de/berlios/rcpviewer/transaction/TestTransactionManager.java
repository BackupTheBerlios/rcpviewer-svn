package de.berlios.rcpviewer.transaction;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.transaction.internal.TransactionManager;
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
