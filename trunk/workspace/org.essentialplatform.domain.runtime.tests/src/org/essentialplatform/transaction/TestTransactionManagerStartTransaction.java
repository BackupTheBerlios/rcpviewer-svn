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
import org.essentialplatform.transaction.TestTransactionManagerStartTransactionInstantiate;

/**
 * @see TestTransactionManagerStartTransactionInstantiate
 * @author Dan Haywood
 *
 */
public class TestTransactionManagerStartTransaction extends AbstractTransactionManagerTestCase {

	public TestTransactionManagerStartTransaction() {
		super(); // setup objects, please.
	}

	public void testDeletingAnObjectImplicitlyStartsATransaction() {
		assertEquals(0, transactionManager.getCurrentTransactions().size());
		calculator.delete();
		assertEquals(1, transactionManager.getCurrentTransactions().size());
	}	
	

	public void testInvokingAnOperationExplicitlyStartsATransaction() {
		assertEquals(0, transactionManager.getCurrentTransactions().size());
		calculator.computeFactorial(5);
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertSame(ITransaction.State.IN_PROGRESS, transaction.getState());
	}


	public void testSettingAnAttributeImplicitlyStartsATransaction() {
		assertEquals(0, transactionManager.getCurrentTransactions().size());
		calculator.setInitialResult(10);
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		assertSame(ITransaction.State.IN_PROGRESS, transaction.getState());
	}

	public void testSettingAOneToOneReferenceImplicitlyStartsATransaction() {
		assertEquals(0, transactionManager.getCurrentTransactions().size());
		customer.setEmailAddress(emailAddress);
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertSame(ITransaction.State.IN_PROGRESS, transaction.getState());
	}

	public void testAddingToACollectionReferenceImplicitlyStartsATransaction() {
		assertEquals(0, transactionManager.getCurrentTransactions().size());
		customer.addToOrders(order);
		assertEquals(1, transactionManager.getCurrentTransactions().size());

		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertSame(ITransaction.State.IN_PROGRESS, transaction.getState());
	}

	public void testRemovingFromACollectionReferenceImplicitlyStartsATransaction() {
		// add the order first, then commit
		customer.addToOrders(order);
		transactionManager.getCurrentTransactionFor(customer, false).commit();

		// now do the remove
		assertEquals(0, transactionManager.getCurrentTransactions().size());
		customer.removeFromOrders(order);
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		
		// the transaction should be in progress and have enrolled both pojos
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertSame(ITransaction.State.IN_PROGRESS, transaction.getState());
	}

	/**
	 * Not yet implemented.
	 */
	public void incompletetestRemovingFromACollectionUsingIteratorReferenceImplicitlyStartsATransaction() {
		// add the order first, then commit
		customer.addToOrders(order);
		transactionManager.getCurrentTransactionFor(customer, false).commit();

		// now do the remove
		assertEquals(0, transactionManager.getCurrentTransactions().size());
		Iterator iter = customer.getOrders().iterator();
		iter.remove();
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		
		// the transaction should be in progress and have enrolled both pojos
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer, false);
		assertSame(ITransaction.State.IN_PROGRESS, transaction.getState());
	}



	// ACCESSING DOES NOT START

	public void testGettingAnAttributeDoesNotStartATransaction() {
		assertEquals(0, transactionManager.getCurrentTransactions().size());
		calculator.getResult();   // don't care about the result
		assertEquals(0, transactionManager.getCurrentTransactions().size());
	}

	public void testGettingAOneToOneReferenceDoesNotStartATransaction() {
		assertEquals(0, transactionManager.getCurrentTransactions().size());
		customer.getEmailAddress();  // don't care about the result
		assertEquals(0, transactionManager.getCurrentTransactions().size());
	}

	public void testGettingACollectionReferenceDoesNotStartATransaction() {
		assertEquals(0, transactionManager.getCurrentTransactions().size());
		customer.getOrders();   // don't care about the result
		assertEquals(0, transactionManager.getCurrentTransactions().size());
	}

	public void testGettingAnIteratorDoesNotStartATransaction() {
		assertEquals(0, transactionManager.getCurrentTransactions().size());
		Iterator iter = customer.getOrders().iterator();
		iter.hasNext();
		assertEquals(0, transactionManager.getCurrentTransactions().size());
	}

}
