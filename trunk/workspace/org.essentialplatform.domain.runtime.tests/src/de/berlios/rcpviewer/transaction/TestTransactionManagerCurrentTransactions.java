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
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.transaction.internal.TransactionManager;
import junit.framework.TestCase;

public class TestTransactionManagerCurrentTransactions extends AbstractTransactionManagerTestCase {

	public TestTransactionManagerCurrentTransactions() {
		super(); // setup objects, please.
	}

	public void testNoCurrentTransactionsInitially() {
		assertEquals(0, TransactionManager.instance().getCurrentTransactions().size());
	}


	// IMPLICITLY START, COMMIT

	public void testCommitTransactionClearsCurrentTransaction() {
		IDomainClass domainClass = 
			(IDomainClass)Domain.lookupAny(Calculator.class);
		domain.addBuilder(new ExtendedProgModelDomainBuilder());
		domain.done();
		
		IDomainObject<Calculator> domainObject = 
			(IDomainObject<Calculator>)session.create(domainClass);
		Calculator calculator = domainObject.getPojo();
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		transaction.commit();
		assertEquals(0, transactionManager.getCurrentTransactions().size());
	}


}
