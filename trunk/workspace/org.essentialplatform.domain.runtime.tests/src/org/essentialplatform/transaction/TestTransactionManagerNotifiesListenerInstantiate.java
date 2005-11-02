package org.essentialplatform.transaction;

import org.essentialplatform.session.IDomainObject;

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
