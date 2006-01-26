package org.essentialplatform.runtime.shared.tests.transaction;

import java.util.Set;

import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.tests.transaction.fixture.Customer;

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
		IDomainObject<Customer> domainObject = clientSession.create(customerDomainClass);
		Customer customer = domainObject.getPojo();
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer);
		Set<IPojo> enlistedPojos = transaction.getEnlistedPojos();
		assertEquals(1, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(customer));
	}

}
