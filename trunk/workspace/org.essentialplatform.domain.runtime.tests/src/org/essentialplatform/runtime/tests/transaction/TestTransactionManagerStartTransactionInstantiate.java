package org.essentialplatform.runtime.tests.transaction;

import org.essentialplatform.runtime.session.IDomainObject;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.transaction.Customer;

/**
 * @see TestTransactionManagerStartTransaction
 * @author Dan Haywood
 */
public class TestTransactionManagerStartTransactionInstantiate extends AbstractTransactionManagerTestCase {

	public TestTransactionManagerStartTransactionInstantiate() {
		super(false); // don't bother to setup any objects.
	}

	public void testInstantiatingAnObjectImplicitlyStartsATransaction() {
		IDomainObject<Customer> domainObject = session.create(customerDomainClass);
		Customer customer = domainObject.getPojo();
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer);
		assertNotNull(transaction);
		assertSame(ITransaction.State.IN_PROGRESS, transaction.getState());
	}


}
