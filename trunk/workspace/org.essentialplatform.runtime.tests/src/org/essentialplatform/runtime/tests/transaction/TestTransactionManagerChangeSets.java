package org.essentialplatform.runtime.tests.transaction;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.changes.Interaction;

public class TestTransactionManagerChangeSets extends AbstractTransactionManagerTestCase {

	public TestTransactionManagerChangeSets() {
		super(); // setup objects, please.
	}

	public void testInvokingAnOperationAddsChangeSet() {
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));
		calculator.computeFactorial(5);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator);
		assertNotNull(transaction);

		List<Interaction> interactions = transaction.getUndoableChanges();
		assertEquals(1, interactions.size());
		interactions.get(0).getModifiedPojos().contains(calculator);
	}


	public void testSettingAnAttributeAddsChangeSet() {
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));
		calculator.setInitialResult(10);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);

		List<Interaction> interactions = transaction.getUndoableChanges();
		assertEquals(1, interactions.size());
		interactions.get(0).getModifiedPojos().contains(calculator);
	}

	public void testSettingAOneToOneReferenceAddsChangeSet() {
		assertNull(transactionManager.getCurrentTransactionFor(calculator, false));
		customer.setEmailAddress(emailAddress);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);

		List<Interaction> interactions = transaction.getUndoableChanges();
		assertEquals(1, interactions.size());
		interactions.get(0).getModifiedPojos().contains(customer);
		interactions.get(0).getModifiedPojos().contains(emailAddress);
	}

	public void testAddingToACollectionReferenceAddsChangeSet() {
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		customer.addToOrders(order);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);

		List<Interaction> interactions = transaction.getUndoableChanges();
		assertEquals(1, interactions.size());
		interactions.get(0).getModifiedPojos().contains(customer);
		interactions.get(0).getModifiedPojos().contains(emailAddress);
	}

	public void testRemovingFromACollectionReferenceAddsChangeSet() {
		// add order then commit
		customer.addToOrders(order);
		transactionManager.getCurrentTransactionFor(customer, false).commit();

		// now do the remove
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		customer.removeFromOrders(order);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);

		List<Interaction> interactions = transaction.getUndoableChanges();
		assertEquals(1, interactions.size());
		interactions.get(0).getModifiedPojos().contains(customer);
		interactions.get(0).getModifiedPojos().contains(emailAddress);
	}

	/**
	 * Not yet implemented.
	 */
	public void incompletetestRemovingFromACollectionUsingIteratorAddsChangeSet() {
		// add order then commit
		customer.addToOrders(order);
		transactionManager.getCurrentTransactionFor(customer, false).commit();

		// now do the remove
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		Iterator iter = customer.getOrders().iterator();
		iter.remove();
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertNotNull(transaction);

		List<Interaction> interactions = transaction.getUndoableChanges();
		assertEquals(1, interactions.size());
		interactions.get(0).getModifiedPojos().contains(customer);
		interactions.get(0).getModifiedPojos().contains(emailAddress);
	}

	public void testDeletingAnObjectAddsChangeSet() {
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		calculator.delete();
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertNotNull(transaction);

		List<Interaction> interactions = transaction.getUndoableChanges();
		assertEquals(1, interactions.size());
		interactions.get(0).getModifiedPojos().contains(calculator);
	}	

	/**
	 * If make a succession of changes (in different interactions) on a pojo, all go into same transaction.
	 */
	public void testMultipleChangesForSamePojoPutInSameTransaction() {
		// do operation #1 
		calculator.computeFactorial(5);
		assertEquals(120, calculator.getResult());
		ITransaction transaction1 = transactionManager.getCurrentTransactionFor(calculator, false);
		assertEquals(1, transaction1.getUndoableChanges().size());

		// set attribute #2 
		calculator.setInitialResult(35);
		assertEquals(35, calculator.getResult());
		ITransaction transaction2 = transactionManager.getCurrentTransactionFor(calculator, false);
		assertSame(transaction1, transaction2);
		assertEquals(2, transaction1.getUndoableChanges().size());
	}

	/**
	 * If an operation modifies multiple pojos, then all the changes will be
	 * added to a single changeset.
	 */
	public void testAllChangesForOperationAddedToSingleChangeSet() {
		
		// no xactns to start with
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		assertNull(transactionManager.getCurrentTransactionFor(emailAddress, false));
		
		// invoke operation on customer; will update the email address as a result
		customer.useEmailAddress(emailAddress);
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

	

}
