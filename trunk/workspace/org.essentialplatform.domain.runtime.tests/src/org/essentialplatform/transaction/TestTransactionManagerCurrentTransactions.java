package org.essentialplatform.transaction;

import org.essentialplatform.domain.Domain;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.session.IDomainObject;
import org.essentialplatform.transaction.internal.TransactionManager;

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
