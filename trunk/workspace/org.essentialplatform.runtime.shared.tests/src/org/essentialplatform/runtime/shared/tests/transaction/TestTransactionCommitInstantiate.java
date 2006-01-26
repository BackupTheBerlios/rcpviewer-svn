package org.essentialplatform.runtime.shared.tests.transaction;

import java.util.Set;

import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.tests.transaction.fixture.Customer;

/**
 * TODO: need additional tests for commits following other alterations (though
 * other tests do this implicitly).
 * 
 * @author Dan Haywood
 */
public class TestTransactionCommitInstantiate extends AbstractTransactionManagerTestCase {

	public TestTransactionCommitInstantiate() {
		super(false); // don't bother to setup any objects.
	}

	public void testCanCommitATransactionForNewlyInstantiatedObject() {

		IDomainObject<Customer> domainObject = clientSession.create(customerDomainClass);
		Customer customer = domainObject.getPojo();
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer);
		
		assertSame(ITransaction.State.IN_PROGRESS, transaction.getState());
		Set<IPojo> enlistedPojos = transaction.getEnlistedPojos();
		assertEquals(1, enlistedPojos.size());
		assertTrue(enlistedPojos.contains(customer));
		
		transaction.commit();
		assertNull(transactionManager.getCurrentTransactionFor(customer, false));
	}

}
