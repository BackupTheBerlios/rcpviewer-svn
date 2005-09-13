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

/**
 * Tests for instantiating are separated out because they have a different
 * fixture.
 * 
 * @author Dan Haywood
 *
 */
public class TestTransactionManagerInstantiate extends AbstractTransactionManagerTestCase {

	public TestTransactionManagerInstantiate() {
		super(false); // don't bother to setup any objects.
	}

	/**
	 * This is also testing getCurrentTransactionsFor, getEnlistedPojos
	 *
	 */
	public void testInstantiatingAnObjectImplicitlyStartsATransaction() {
		IDomainObject<Customer> domainObject = 
			(IDomainObject<Customer>)session.create(customerDomainClass);
		Customer customer = domainObject.getPojo();
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer);

		assertSame(ITransaction.State.IN_PROGRESS, transaction.getState());
		Set<ITransactable> enlistedPojos = transaction.getEnlistedPojos();
		assertEquals(1, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(customer));
	}

	/**
	 * Make sure we can commit okay.  That's because we do this in the setup
	 * of all the other test cases, and I want to be debugging tests, not
	 * fixtures...
	 *
	 */
	public void testCanCommitATransaction() {

		IDomainObject<Customer> domainObject = 
			(IDomainObject<Customer>)session.create(customerDomainClass);
		Customer customer = domainObject.getPojo();
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer);
		
		assertSame(ITransaction.State.IN_PROGRESS, transaction.getState());
		Set<ITransactable> enlistedPojos = transaction.getEnlistedPojos();
		assertEquals(1, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(customer));
		
		transaction.commit();
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
		
	}

}
