package de.berlios.rcpviewer.transaction;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.transaction.internal.TransactionManager;
import junit.framework.TestCase;

public class TestTransactionManagerReverseReapply extends AbstractTransactionManagerTestCase {

	public TestTransactionManagerReverseReapply() {
		super(); // setup objects, please.
	}


	public void testTransactionId() {
		calculator.computeFactorial(5);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		String transactionId = transaction.id(); 
		assertNotNull(transactionId);
		assertFalse("".equals(transactionId));
	}

	public void testCommitRemovesFromTransactionManagersCurrentTransactions() {
		calculator.computeFactorial(5);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		transaction.commit();
		assertEquals(0, transactionManager.getCurrentTransactions().size());
	}

	public void testCommitAddsToTransactionManagersCommittedTransactions() {
		calculator.computeFactorial(5);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		assertEquals(0, transactionManager.getCommittedTransactions().size());
		transaction.commit();
		assertEquals(1, transactionManager.getCommittedTransactions().size());
	}

	public void testInvokingAnOperationReverseReapply() {
		ITransaction transaction;
		String transactionId;
		
		// do xactn #1 and commit
		calculator.computeFactorial(5);
		assertEquals(120, calculator.getResult());
		transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		transaction.commit();
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));

		// do xactn #2  and commit 
		calculator.computeFactorial(6);
		assertEquals(720, calculator.getResult());
		transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		transactionId = transaction.id();
		assertNotNull(transaction);
		transaction.commit();
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));

		// reverse last xactn; back to state after xactn #1
		transactionManager.reverse(transactionId);
		assertEquals(120, calculator.getResult());
		
		// reapply last xactn; back to state after xactn #2 
		transactionManager.reapply(transactionId);
		assertEquals(720, calculator.getResult());
	}

	public void testIsIrreversibleForReversibleTransaction() {
		ITransaction transaction;
		String transactionId;
		
		// change #1: regular operation
		calculator.computeFactorial(5);
		
		// change #1: irreversible operation (added "by hand")
		transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		
		// attempt to reverse; should fail.
		assertTrue(transaction.isIrreversible());
	}

	public void testIsIrreversibleForIrreversibleTransaction() {
		ITransaction transaction;
		String transactionId;
		
		// change #1: regular operation
		calculator.computeFactorial(5);
		
		// change #2: irreversible operation (added "by hand")
		transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		transaction.startingInteraction();
		transaction.addingToInteractionChangeSet(IChange.IRREVERSIBLE);
		transaction.completingInteraction();
		transactionId = transaction.id();
		
		// do the commit
		transaction.commit();
		
		// attempt to reverse; should fail.
		assertTrue(transaction.isIrreversible());
	}

	public void testIrreversibleTransactionsAreStillAddedToCommittedTransactions() {
		ITransaction transaction;
		String transactionId;
		
		// change #1: regular operation
		calculator.computeFactorial(5);
		
		// change #2: irreversible operation (added "by hand")
		transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		transaction.startingInteraction();
		transaction.addingToInteractionChangeSet(IChange.IRREVERSIBLE);
		transaction.completingInteraction();
		
		// do the commit; should be added to committed xactns as per usual
		assertEquals(0, transactionManager.getCommittedTransactions().size());
		transaction.commit();
		assertEquals(1, transactionManager.getCommittedTransactions().size());
	}

	public void testCannotReverseAnIrreversibleTransaction() {
		ITransaction transaction;
		String transactionId;
		
		// change #1: regular operation
		calculator.computeFactorial(5);
		
		// change #2: irreversible operation (added "by hand")
		transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		transaction.startingInteraction();
		transaction.addingToInteractionChangeSet(IChange.IRREVERSIBLE);
		transaction.completingInteraction();
		transactionId = transaction.id();
		
		// do the commit
		transaction.commit();
		
		// attempt to reverse; should fail.
		try {
			transactionManager.reverse(transactionId);
			fail("Expected IrreversibleTransactionException to have been thrown.");
		} catch(IrreversibleTransactionException ex) {
			// expected
		} catch(Exception ex) {
			fail("Expected RuntimeException to have been thrown.");
		}
	}

	/**
	 * TODO: need an @Irreversible annotation for operations.
	 *
	 */
	public void incompletetestCannotReverseAnIrreversibleTransaction() {
		ITransaction transaction;
		String transactionId;
		
		// do xactn #1 and commit
		calculator.computeFactorial(5);
		// calculator.irreversibleOperation(); - need to write and annotate as @Irreversible
		
		assertEquals(120, calculator.getResult());
		transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		
		transaction.commit();
	}

}
