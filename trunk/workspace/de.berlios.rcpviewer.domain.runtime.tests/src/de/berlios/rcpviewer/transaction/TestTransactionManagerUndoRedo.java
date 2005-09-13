package de.berlios.rcpviewer.transaction;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.transaction.internal.TransactionManager;
import junit.framework.TestCase;

public class TestTransactionManagerUndoRedo extends AbstractTransactionManagerTestCase {

	public TestTransactionManagerUndoRedo() {
		super(); // setup objects, please.
	}


	
	public void testInvokingAnOperationUndoRedo() {
		// do operation #1 
		calculator.computeFactorial(5);
		assertEquals(120, calculator.getResult());

		// do operation #2 
		calculator.computeFactorial(6);
		assertEquals(720, calculator.getResult());

		// undo last change
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		
		// undo; back to state after operation #1
		transaction.undoPendingChange();
		assertEquals(120, calculator.getResult());

		// redo last change; back to state after operation #2 
		transaction.redoPendingChange();
		assertEquals(720, calculator.getResult());
	}

	public void testInvokingAnOperationRedoAll() {
		// do operation #1, #2, #3 
		calculator.computeFactorial(5);
		assertEquals(120, calculator.getResult());
		calculator.computeFactorial(6);
		assertEquals(720, calculator.getResult());
		calculator.computeFactorial(3);
		assertEquals(6, calculator.getResult());
		
		// undo operations #3, #2
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		transaction.undoPendingChange();
		assertEquals(120, calculator.getResult());
		
		// redo all
		transaction.redoPendingChanges();
		assertEquals(6, calculator.getResult());
	}

	public void testSettingAnAttributeUndoRedo() {
		// set attribute #1
		calculator.setInitialResult(10);
		assertEquals(10, calculator.getResult());
		
		// set attribute #2
		calculator.setInitialResult(20);
		assertEquals(20, calculator.getResult());
		
		// undo last change; back to state after set attribute #1
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		assertEquals(10, calculator.getResult());

		// redo last change; back to state after set attribute #2
		transaction.redoPendingChange();
		assertEquals(20, calculator.getResult());
	}

	public void testSettingAnAttributeRedoAll() {
		// do set attribute #1, #2, #3 
		calculator.setInitialResult(10);
		calculator.setInitialResult(20);
		calculator.setInitialResult(30);
		assertEquals(30, calculator.getResult());
		
		// undo set attribute #3, #2
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		transaction.undoPendingChange();
		assertEquals(10, calculator.getResult());
		
		// redo all
		transaction.redoPendingChanges();
		assertEquals(30, calculator.getResult());
	}

	public void testSettingAOneToOneReferenceUndoRedo() {
		// set reference #1
		customer.setEmailAddress(emailAddress);
		assertSame(emailAddress, customer.getEmailAddress());

		// set reference #2
		customer.setEmailAddress(emailAddress2);
		assertSame(emailAddress2, customer.getEmailAddress());

		// undo last change; back to state after set reference #1
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		assertSame(emailAddress, customer.getEmailAddress());

		// redo last change; back to state after set reference #2
		transaction.redoPendingChange();
		assertSame(emailAddress2, customer.getEmailAddress());
	}

	public void testSettingAReferenceRedoAll() {
		// do set reference #1, #2, #3 
		customer.setEmailAddress(emailAddress);
		customer.setEmailAddress(emailAddress2);
		customer.setEmailAddress(emailAddress3);
		assertSame(emailAddress3, customer.getEmailAddress());
		
		// undo set reference #3, #2
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		transaction.undoPendingChange();
		assertSame(emailAddress, customer.getEmailAddress());
		
		// redo all
		transaction.redoPendingChanges();
		assertSame(emailAddress3, customer.getEmailAddress());
	}

	public void testAddingToACollectionReferenceUndo() {
		// add reference #1
		customer.addToOrders(order);
		assertTrue(customer.getOrders().contains(order));
		assertFalse(customer.getOrders().contains(order2));
		
		// add reference #2
		customer.addToOrders(order2);
		assertTrue(customer.getOrders().contains(order));
		assertTrue(customer.getOrders().contains(order2));
		
		// undo last change; back to state after set reference #1
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		assertTrue(customer.getOrders().contains(order));
		assertFalse(customer.getOrders().contains(order2));
	}

	public void testAddingToACollectionReferenceRedo() {
		// add reference #1
		customer.addToOrders(order);
		assertTrue(customer.getOrders().contains(order));
		assertFalse(customer.getOrders().contains(order2));
		
		// add reference #2
		customer.addToOrders(order2);
		assertTrue(customer.getOrders().contains(order));
		assertTrue(customer.getOrders().contains(order2));
		
		// undo last change; back to state after set reference #1
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		assertTrue(customer.getOrders().contains(order));
		assertFalse(customer.getOrders().contains(order2));

		// redo last change; back to state after set reference #2
		transaction.redoPendingChange();
		assertTrue(customer.getOrders().contains(order));
		assertTrue(customer.getOrders().contains(order2));
	}

	public void testAddingToACollectionRedoAll() {
		// do add to collection #1, #2, #3 
		customer.addToOrders(order);
		customer.addToOrders(order2);
		customer.addToOrders(order3);
		assertTrue(customer.getOrders().contains(order));
		assertTrue(customer.getOrders().contains(order2));
		assertTrue(customer.getOrders().contains(order3));
		
		// undo #3, #2
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		transaction.undoPendingChange();
		assertTrue(customer.getOrders().contains(order));
		assertFalse(customer.getOrders().contains(order2));
		assertFalse(customer.getOrders().contains(order3));
		
		// redo all
		transaction.redoPendingChanges();
		assertTrue(customer.getOrders().contains(order));
		assertTrue(customer.getOrders().contains(order2));
		assertTrue(customer.getOrders().contains(order3));
	}


	public void testRemovingFromACollectionReferenceRedoAll() {
		// to set up, add reference
		customer.addToOrders(order);
		customer.addToOrders(order2);
		customer.addToOrders(order3);
		assertTrue(customer.getOrders().contains(order));
		assertTrue(customer.getOrders().contains(order2));
		assertTrue(customer.getOrders().contains(order3));
		
		// remove reference
		customer.removeFromOrders(order2);
		customer.removeFromOrders(order);
		assertFalse(customer.getOrders().contains(order));
		assertFalse(customer.getOrders().contains(order2));
		assertTrue(customer.getOrders().contains(order3));
		
		// undo last change x2 
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		transaction.undoPendingChange();
		assertTrue(customer.getOrders().contains(order));
		assertTrue(customer.getOrders().contains(order2));
		assertTrue(customer.getOrders().contains(order3));
		
		// redo all 
		transaction.redoPendingChanges();
		assertFalse(customer.getOrders().contains(order));
		assertFalse(customer.getOrders().contains(order2));
		assertTrue(customer.getOrders().contains(order3));
	}

	public void testRemovingFromACollectionReferenceUndoRedo() {
		// to set up, add reference
		customer.addToOrders(order);
		assertTrue(customer.getOrders().contains(order));
		
		// remove reference
		customer.removeFromOrders(order);
		assertFalse(customer.getOrders().contains(order));
		
		// undo last change; back to state after initial (reference contained) 
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		assertTrue(customer.getOrders().contains(order));
		
		// redo last change; back to state after remove (reference removed) 
		transaction.undoPendingChange();
		assertFalse(customer.getOrders().contains(order));
	}

	/**
	 * Not yet supported.
	 *
	 */
	public void incompletetestRemovingFromACollectionUsingIteratorUndoRedo() {
		// to set up, add reference
		customer.addToOrders(order);
		assertTrue(customer.getOrders().contains(order));
		
		// remove reference through iterator
		Iterator iter = customer.getOrders().iterator();
		iter.remove();
		assertFalse(customer.getOrders().contains(order));
		
		// undo last change; back to state after initial (reference contained) 
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		assertTrue(customer.getOrders().contains(order));
		
		// redo last change; back to state after remove (reference removed) 
		transaction.undoPendingChange();
		assertFalse(customer.getOrders().contains(order));
	}


	/**
	 * Not yet supported.
	 *
	 */
	public void incompletetestRemovingFromACollectionReferenceUsingIteratorRedoAll() {
		// to set up, add reference
		customer.addToOrders(order);
		customer.addToOrders(order2);
		customer.addToOrders(order3);
		assertTrue(customer.getOrders().contains(order));
		assertTrue(customer.getOrders().contains(order2));
		assertTrue(customer.getOrders().contains(order3));
		
		// remove reference
		Iterator iter = customer.getOrders().iterator();
		iter.remove();
		iter.remove();
		assertFalse(customer.getOrders().contains(order));
		assertFalse(customer.getOrders().contains(order2));
		assertTrue(customer.getOrders().contains(order3));
		
		// undo last change x2 
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		transaction.undoPendingChange();
		assertTrue(customer.getOrders().contains(order));
		assertTrue(customer.getOrders().contains(order2));
		assertTrue(customer.getOrders().contains(order3));
		
		// redo all 
		transaction.redoPendingChanges();
		assertFalse(customer.getOrders().contains(order));
		assertFalse(customer.getOrders().contains(order2));
		assertTrue(customer.getOrders().contains(order3));
	}

	public void testDeletingAnObjectUndoRedo() {
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));
		calculator.setInitialResult(10); // so when undo there is still a change
		calculator.delete();
		assertFalse(calculatorDomainObject.getPersistState().isPersistent());
		
		// undo last change
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		assertTrue(calculatorDomainObject.getPersistState().isPersistent());
		
		// redo last change
		transaction.redoPendingChange();
		assertFalse(calculatorDomainObject.getPersistState().isPersistent());
	}	

	public void testUndoAllDiscardsTheTransaction() {
		// do operation #1 
		calculator.computeFactorial(5);
		assertEquals(120, calculator.getResult());

		// set attribute #2
		calculator.setInitialResult(20);
		assertEquals(20, calculator.getResult());
		
		// do operation #3
		calculator.computeFactorial(6);
		assertEquals(720, calculator.getResult());

		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		assertEquals(3, transaction.getUndoableChanges().size());

		// undo last change
		transaction.undoPendingChange();
		assertEquals(2, transaction.getUndoableChanges().size());

		// undo last change
		transaction.undoPendingChange();
		assertEquals(1, transaction.getUndoableChanges().size());

		// undo last change
		transaction.undoPendingChange();
		
		// the following would throw an exception since the transaction is
		// no longer current:
//		assertEquals(0, transaction.getUndoableChanges().size());
		
		assertSame(ITransaction.State.DISCARDED, transaction.getState());
	}
	

	/**
	 * If an operation modifies multiple pojos, then all the changes will be
	 * added to a single changeset.  This is the corollary: those changes should
	 * be undone in one go.
	 */
	public void testAllChangesForOperationUndoneAsSingleUnit() {
		
		// no xactns to start with
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		assertNull(transactionManager.getCurrentTransactionFor(emailAddress, false));
		
		// initial state
		assertNull(customer.getEmailAddress());
		assertNull(emailAddress.getCustomer());
		
		// invoke operation on customer; will update the email address as a result
		customer.useEmailAddress(emailAddress);
		assertNotNull(customer.getEmailAddress());
		assertNotNull(emailAddress.getCustomer());
		
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertEquals(1, transaction.getUndoableChanges().size());

		// undo last change
		transaction.undoPendingChange();
		
		// back to initial state
		assertNull(customer.getEmailAddress());
		assertNull(emailAddress.getCustomer());
	}

	/**
	 *
	 */
	public void testPerformingChangeClearsRedoStack() {
		// do operation #1, #2, #3 
		calculator.computeFactorial(5);
		assertEquals(120, calculator.getResult());
		calculator.computeFactorial(6);
		assertEquals(720, calculator.getResult());
		calculator.computeFactorial(3);
		assertEquals(6, calculator.getResult());
		
		// undo operations #3, #2
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		transaction.undoPendingChange();
		transaction.undoPendingChange();
		
		// 2 undone changes that could be redone.
		assertEquals(2, transaction.getRedoableChanges().size());
		
		calculator.setInitialResult(20);
		
		// undone changes are now gone.
		assertEquals(0, transaction.getRedoableChanges().size());
	}


}
