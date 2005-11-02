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
		domain.addBuilder(new EssentialProgModelExtendedSemanticsDomainBuilder());
		domain.done();
		
		IDomainObject<?> domainObject = session.create(domainClass);
		Calculator calculator = (Calculator)domainObject.getPojo();
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		transaction.commit();
		assertEquals(0, transactionManager.getCurrentTransactions().size());
	}


}
