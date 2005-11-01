package de.berlios.rcpviewer.transaction;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.transaction.internal.TransactionManager;
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
