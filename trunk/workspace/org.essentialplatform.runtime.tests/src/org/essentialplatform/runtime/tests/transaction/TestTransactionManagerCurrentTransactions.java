package org.essentialplatform.runtime.tests.transaction;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.session.IDomainObject;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.internal.TransactionManager;
import org.essentialplatform.transaction.Calculator;

public class TestTransactionManagerCurrentTransactions extends AbstractTransactionManagerTestCase {

	public TestTransactionManagerCurrentTransactions() {
		super(); // setup objects, please.
	}

	public void testNoCurrentTransactionsInitially() {
		assertEquals(0, TransactionManager.instance().getCurrentTransactions().size());
	}


	// IMPLICITLY START, COMMIT

	public void testCommitTransactionClearsCurrentTransaction() {
		IDomainClass domainClass = Domain.lookupAny(Calculator.class);
		
		IDomainObject<Calculator> domainObject = session.create(domainClass);
		Calculator calculator = domainObject.getPojo();
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		ITransaction transaction = transactionManager.getCurrentTransactionFor(calculator, false);
		transaction.commit();
		assertEquals(0, transactionManager.getCurrentTransactions().size());
	}


}