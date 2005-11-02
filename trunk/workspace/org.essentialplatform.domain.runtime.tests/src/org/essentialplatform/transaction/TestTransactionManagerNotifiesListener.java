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
public class TestTransactionManagerNotifiesListener extends AbstractTransactionManagerTestCase {

	private MyTransactionManagerListener listener = new MyTransactionManagerListener(); 

	public TestTransactionManagerNotifiesListener() {
		super(true);
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
	
	public void testCommittingTransactionNotifiesTransactionManagerListeners() {
		assertNull(listener.committedTransactionEvent); 
		calculator.computeFactorial(5);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		transaction.commit();
		assertNotNull(transaction);
		TransactionManagerEvent event = listener.committedTransactionEvent;
		assertNotNull(event);
		assertSame(transactionManager, event.getTransactionManager());
		assertSame(transaction, event.getTransaction());
	}

	public void testDiscardingTransactionImplicitlyNotifiesTransactionManagerListeners() {
		assertNull(listener.discardedTransactionEvent); 
		calculator.computeFactorial(5);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		transaction.undoPendingChange();
		TransactionManagerEvent event = listener.discardedTransactionEvent;
		assertNotNull(event);
		assertSame(transactionManager, event.getTransactionManager());
		assertSame(transaction, event.getTransaction());
	}

	public void testDiscardingTransactionExplicitlyNotifiesTransactionManagerListeners() {
		assertNull(listener.discardedTransactionEvent); 
		calculator.computeFactorial(5);
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		transaction.discard();
		TransactionManagerEvent event = listener.discardedTransactionEvent;
		assertNotNull(event);
		assertSame(transactionManager, event.getTransactionManager());
		assertSame(transaction, event.getTransaction());
	}

	public void incompletetestReapplyingTransactionNotifiesTransactionManagerListeners() {
		assertNull(listener.reappliedTransactionEvent); 


	}

	public void incompletetestReversingTransactionNotifiesTransactionManagerListeners() {
		assertNull(listener.reversedTransactionEvent); 

	}

}
