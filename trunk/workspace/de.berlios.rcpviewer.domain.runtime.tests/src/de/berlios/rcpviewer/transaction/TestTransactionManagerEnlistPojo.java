package de.berlios.rcpviewer.transaction;

import java.util.Iterator;
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
import de.berlios.rcpviewer.transaction.TestTransactionManagerEnlistPojoInstantiate;

/**
 * Modifying pojos enrols them in the transaction.
 * 
 * @see TestTransactionManagerEnlistPojoInstantiate
 * @author Dan Haywood
 *
 */
public class TestTransactionManagerEnlistPojo extends AbstractTransactionManagerTestCase {

	public TestTransactionManagerEnlistPojo() {
		super(); // setup objects, please.
	}


	/**
	 * rather dubious since we have this in the fixture...
	 */
	public void testGetCurrentTransactionsForNonEnlistedPojoReturnsNothing() {
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));
	}

	/**
	 * rather dubious since we have this in the fixture...
	 */
	public void testGetCurrentTransactionsForEnlistedPojoReturnsTransaction() {
		calculator.computeFactorial(5);
		assertNotNull(transactionManager.getCurrentTransactionFor(calculator, false));
	}


	public void testInvokingAnOperationEnlistedPojo() {
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));
		calculator.computeFactorial(5);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		
		Set<ITransactable> enlistedPojos = transaction.getEnlistedPojos();
		assertEquals(1, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(calculator));
	}


	public void testSettingAnAttributeEnlistedPojo() {
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));
		calculator.setInitialResult(10);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		
		Set<ITransactable> enlistedPojos = transaction.getEnlistedPojos();
		assertEquals(1, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(calculator));
	}

	public void testSettingAOneToOneReferenceEnlistsReferringPojoAndAlsoReferencedPojo() {
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));
		customer.setEmailAddress(emailAddress);
		ITransaction customerTransaction = transactionManager.getCurrentTransactionFor(customer, false);
		ITransaction emailAddressTransaction = transactionManager.getCurrentTransactionFor(emailAddress, false);
		assertNotNull(customerTransaction);
		assertNotNull(emailAddressTransaction);
		assertSame(customerTransaction, emailAddressTransaction);
		
		Set<ITransactable> enlistedPojos = customerTransaction.getEnlistedPojos();
		assertEquals(2, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(customer));
		assertTrue(enlistedPojos.contains(emailAddress));
	}

	public void testAddingToACollectionReferenceEnlistsCollectionOwnerAndAlsoReferencedObject() {
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		customer.addToOrders(order);
		ITransaction customerTransaction = transactionManager.getCurrentTransactionFor(customer, false);
		ITransaction orderTransaction = transactionManager.getCurrentTransactionFor(order, false);
		assertNotNull(customerTransaction);
		assertNotNull(orderTransaction);
		assertSame(customerTransaction, orderTransaction);

		Set<ITransactable> enlistedPojos = customerTransaction.getEnlistedPojos();
		assertEquals(2, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(customer));
		assertTrue(enlistedPojos.contains(order));
	}

	public void testRemovingFromACollectionReferenceEnlistsCollectionOwnerAndAlsoReferencedObject() {
		// add order then commit
		customer.addToOrders(order);
		transactionManager.getCurrentTransactionFor(customer, false).commit();

		// now do the remove
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		customer.removeFromOrders(order);
		ITransaction customerTransaction = transactionManager.getCurrentTransactionFor(customer, false);
		ITransaction orderTransaction = transactionManager.getCurrentTransactionFor(order, false);
		assertNotNull(customerTransaction);
		assertNotNull(orderTransaction);
		assertSame(customerTransaction, orderTransaction);

		Set<ITransactable> enlistedPojos = customerTransaction.getEnlistedPojos();
		assertEquals(2, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(customer));
		assertTrue(enlistedPojos.contains(order));
	}

	/**
	 * Not yet supported.
	 *
	 */
	public void incompletetestRemovingFromACollectionUsingIteratorEnlistsCollectionOwnerAndReferencedObject() {
		// add order then commit
		customer.addToOrders(order);
		transactionManager.getCurrentTransactionFor(customer, false).commit();

		// now do the remove
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		Iterator iter = customer.getOrders().iterator();
		iter.remove();
		ITransaction customerTransaction = transactionManager.getCurrentTransactionFor(customer, false);
		ITransaction orderTransaction = transactionManager.getCurrentTransactionFor(order, false);
		assertNotNull(customerTransaction);
		assertNotNull(orderTransaction);
		assertSame(customerTransaction, orderTransaction);
		
		Set<ITransactable> enlistedPojos = customerTransaction.getEnlistedPojos();
		assertEquals(2, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(customer));
		assertTrue(enlistedPojos.contains(order));
	}

	public void testDeletingAnObjectEnlistsPojo() {
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));
		calculator.delete();
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);

		Set<ITransactable> enlistedPojos = transaction.getEnlistedPojos();
		assertEquals(1, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(calculator));
	}	

	public void incompletetestTransactionSessionIsTakenFromFirstEnlistedPojo() {
		
	}
	
	/**
	 * Make 2 changes on different pojos; both enlisted; undo one change and the
	 * 2nd pojo should be delisted.
	 *
	 */
	public void testUndoingChangeDelistsPojo() {
		assertNull(transactionManager.getCurrentTransactionFor(emailAddress, false));
		
		// 1st change - enlists just the emailAddress
		emailAddress.setEmail("joe@bloggs.com");
		ITransaction emailAddressTransaction = transactionManager.getCurrentTransactionFor(emailAddress, false);
		assertNotNull(emailAddressTransaction);
		assertTrue(emailAddressTransaction.getEnlistedPojos().contains(emailAddress));

		// 2nd change - enlists the customer
		emailAddress.setCustomer(customer);
		ITransaction customerTransaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertSame(emailAddressTransaction, customerTransaction);
		assertTrue(emailAddressTransaction.getEnlistedPojos().contains(emailAddress));
		assertTrue(emailAddressTransaction.getEnlistedPojos().contains(customer));

		// undo; is delisted
		emailAddressTransaction.undoPendingChange();
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		assertTrue(emailAddressTransaction.getEnlistedPojos().contains(emailAddress));
		assertFalse(emailAddressTransaction.getEnlistedPojos().contains(customer));
		
	}


	
	/**
	 * Make 2 changes on different pojos; both enlisted; undo one change and the
	 * 2nd pojo should be delisted.
	 *
	 */
	public void testRedoingChangeRelistsPojo() {
		assertNull(transactionManager.getCurrentTransactionFor(emailAddress, false));
		
		// 1st change - enlists just the emailAddress
		emailAddress.setEmail("joe@bloggs.com");

		// 2nd change - enlists the customer
		emailAddress.setCustomer(customer);

		// undo; is delisted
		ITransaction emailAddressTransaction = transactionManager.getCurrentTransactionFor(emailAddress, false);
		emailAddressTransaction.undoPendingChange();
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		assertTrue(emailAddressTransaction.getEnlistedPojos().contains(emailAddress));
		assertFalse(emailAddressTransaction.getEnlistedPojos().contains(customer));
		
		// redo; is enlisted
		emailAddressTransaction.redoPendingChange();
		ITransaction customerTransaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertSame(emailAddressTransaction, customerTransaction);
		assertTrue(emailAddressTransaction.getEnlistedPojos().contains(emailAddress));
		assertTrue(emailAddressTransaction.getEnlistedPojos().contains(customer));
		
	}

	/**
	 * Make changes on several pojos; enlisted; discard transaction and the
	 * pojos should be delisted.
	 *
	 */
	public void incompletetestDiscardingTransactionDelistsPojos() {
		
	}

	/**
	 * Make 2 changes on same pojo; undo one change but the pojo should remain
	 * enlisted
	 *
	 */
	public void testUndoingChangeDoesntDelistsPojoIfHasBeenChangedPreviouslyInTransaction() {
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));

		// first change; object is enlisted
		calculator.setInitialResult(10);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);
		assertEquals(1, transaction.getUndoableChanges().size());
		assertEquals(1, transaction.getEnlistedPojos().size());
		assertTrue(transaction.getEnlistedPojos().contains(calculator));
		
		// second change
		calculator.computeFactorial(5);
		assertEquals(2, transaction.getUndoableChanges().size());
		assertEquals(1, transaction.getEnlistedPojos().size());
		assertTrue(transaction.getEnlistedPojos().contains(calculator));
		
		// undo second change; object remains enlisted
		transaction.undoPendingChange();
		assertEquals(1, transaction.getUndoableChanges().size());
		assertEquals(1, transaction.getEnlistedPojos().size());
		assertTrue(transaction.getEnlistedPojos().contains(calculator));
	}

	/**
	 * Make changes to different pojos; the transactions are independent.
	 */
	public void testCanHaveTwoConcurrentTransactions() {
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		calculator.setInitialResult(10);
		customer.useEmailAddress(emailAddress);
		ITransaction calculatorTransaction = transactionManager.getCurrentTransactionFor(calculator, false);
		ITransaction customerTransaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(calculatorTransaction);
		assertNotNull(customerTransaction);
		assertNotSame(calculatorTransaction, customerTransaction);
		assertFalse(calculatorTransaction.getEnlistedPojos().contains(customer));
		assertFalse(customerTransaction.getEnlistedPojos().contains(calculator));
	}

	public void testCannotEnlistPojoInTwoConcurrentTransactions() {
		assertNull(transactionManager.getCurrentTransactionFor(emailAddress, false));

		// enlist customer and emailAddress in 1st xactn
		emailAddress.setCustomer(customer);
		ITransaction emailAddressTransaction = transactionManager.getCurrentTransactionFor(emailAddress, false);
		assertNotNull(emailAddressTransaction);
		assertTrue(emailAddressTransaction.getEnlistedPojos().contains(emailAddress));
		assertTrue(emailAddressTransaction.getEnlistedPojos().contains(customer));

		// attempt to enlist customer with emailAddress2 in a 2nd xactn
		assertNull(transactionManager.getCurrentTransactionFor(emailAddress2, false));
		try {
			emailAddress2.setCustomer(customer);
			fail("Should have thrown an exception.");
		} catch(PojoAlreadyEnlistedException ex) {
			// expected
			assertSame(customer, ex.getTransactable());
			assertSame(emailAddressTransaction, ex.getTransaction());
		} catch(Exception ex) {
			fail("Expected to throw SomeVerySpecificException - instead got a " + ex.getClass().getName());
		}
	}
	

}
