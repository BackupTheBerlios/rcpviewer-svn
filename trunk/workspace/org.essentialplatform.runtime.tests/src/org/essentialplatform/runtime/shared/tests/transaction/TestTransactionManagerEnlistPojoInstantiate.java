package org.essentialplatform.runtime.shared.tests.transaction;

import java.util.Set;

import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.transaction.fixture.Customer;
import org.essentialplatform.runtime.shared.transaction.ITransactable;
import org.essentialplatform.runtime.shared.transaction.ITransaction;

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
		IDomainObject<Customer> domainObject = session.create(customerDomainClass);
		Customer customer = domainObject.getPojo();
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer);
		Set<ITransactable> enlistedPojos = transaction.getEnlistedPojos();
		assertEquals(1, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(customer));
	}

}
