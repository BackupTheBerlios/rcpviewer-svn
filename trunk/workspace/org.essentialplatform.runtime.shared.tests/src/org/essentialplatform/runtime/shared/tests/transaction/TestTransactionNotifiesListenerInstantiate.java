package org.essentialplatform.runtime.shared.tests.transaction;


/**
 * 
 * @author Dan Haywood
 */
public class TestTransactionNotifiesListenerInstantiate extends AbstractTransactionManagerTestCase {

	private MyTransactionListener listener = new MyTransactionListener(); 

	public TestTransactionNotifiesListenerInstantiate() {
		super(true);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		listener = new MyTransactionListener(); 
	}
	
	@Override
	public void tearDown() throws Exception {
		listener = null;
		super.tearDown();
	}
	
	/**
	 * This cannot be tested because we are not able to install the listener
	 * on the transaction
	 *
	 */
	public void testInstantiatingObjectNotifiesTransactionListenersOfAddingChange() {
	}


}
