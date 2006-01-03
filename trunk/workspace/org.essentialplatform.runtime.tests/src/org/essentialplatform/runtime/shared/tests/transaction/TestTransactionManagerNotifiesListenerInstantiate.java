package org.essentialplatform.runtime.shared.tests.transaction;

import org.essentialplatform.runtime.client.transaction.event.TransactionManagerEvent;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.transaction.fixture.Customer;
import org.essentialplatform.runtime.shared.transaction.ITransaction;

/**
 * 
 * @author Dan Haywood
 */
public class TestTransactionManagerNotifiesListenerInstantiate extends AbstractTransactionManagerTestCase {

	private MyTransactionManagerListener listener = new MyTransactionManagerListener(); 

	public TestTransactionManagerNotifiesListenerInstantiate() {
		super(false); // don't bother to setup any objects.
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		listener = new MyTransactionManagerListener(); 
		transactionManager.addTransactionManagerListener(listener);
	}
	
	@Override
	public void tearDown() throws Exception {
		transactionManager.removeTransactionManagerListener(listener);
		listener = null;
		super.tearDown();
	}

	public void testInstantiatingAnObjectNotifiesTransactionManagerListenersOfNewTransaction() {
		assertNull(listener.createdTransactionEvent);
		IDomainObject<Customer> domainObject = session.create(customerDomainClass);
		Customer customer = domainObject.getPojo();
		TransactionManagerEvent event = listener.createdTransactionEvent; 
		assertNotNull(event);
		assertSame(transactionManager, event.getTransactionManager());
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer);
		assertSame(transaction, event.getTransaction());
	}

}
