package org.essentialplatform.runtime.tests.transaction;

import java.util.Set;

import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.fixture.transaction.Customer;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;

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

		IDomainObject<Customer> domainObject = session.create(customerDomainClass);
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
