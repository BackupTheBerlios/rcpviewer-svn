package org.essentialplatform.transaction;

import org.essentialplatform.session.IDomainObject;

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
