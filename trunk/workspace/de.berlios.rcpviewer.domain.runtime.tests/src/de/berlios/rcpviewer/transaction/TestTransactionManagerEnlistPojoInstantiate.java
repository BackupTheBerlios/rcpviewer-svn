package de.berlios.rcpviewer.transaction;

import java.util.Set;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * @see TestTransactionManagerEnlistPojo
 * 
 * @author Dan Haywood
 *
 */
public class TestTransactionManagerEnlistPojoInstantiate extends AbstractTransactionManagerTestCase {

	public TestTransactionManagerEnlistPojoInstantiate() {
		super(false); // don't bother to setup any objects.
	}


	public void testInstantiatingAnObjectEnlistsObject() {
		IDomainObject<Customer> domainObject = 
			(IDomainObject<Customer>)session.create(customerDomainClass);
		Customer customer = domainObject.getPojo();
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer);
		Set<ITransactable> enlistedPojos = transaction.getEnlistedPojos();
		assertEquals(1, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(customer));
	}

}
