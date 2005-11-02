package org.essentialplatform.transaction;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.Domain;
import org.essentialplatform.persistence.IObjectStore;
import org.essentialplatform.persistence.inmemory.InMemoryObjectStore;
import org.essentialplatform.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;
import org.essentialplatform.progmodel.standard.ProgModelConstants;
import org.essentialplatform.session.IDomainObject;
import org.essentialplatform.session.ISession;
import org.essentialplatform.session.local.SessionManager;
import org.essentialplatform.transaction.internal.TransactionManager;
import junit.framework.TestCase;

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
		IDomainObject<?> domainObject = session.create(customerDomainClass);
		Customer customer = (Customer)domainObject.getPojo();
		TransactionManagerEvent event = listener.createdTransactionEvent; 
		assertNotNull(event);
		assertSame(transactionManager, event.getTransactionManager());
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer);
		assertSame(transaction, event.getTransaction());
	}

}
