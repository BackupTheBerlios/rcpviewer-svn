package org.essentialplatform.runtime.shared.tests.transaction;

import org.essentialplatform.runtime.client.transaction.event.ITransactionManagerListener;
import org.essentialplatform.runtime.client.transaction.event.TransactionManagerEvent;

public final class MyTransactionManagerListener implements
		ITransactionManagerListener {

	TransactionManagerEvent createdTransactionEvent = null;
	TransactionManagerEvent committedTransactionEvent = null;
	TransactionManagerEvent reversedTransactionEvent = null;
	TransactionManagerEvent reappliedTransactionEvent = null;
	TransactionManagerEvent discardedTransactionEvent = null;
	
	public void createdTransaction(TransactionManagerEvent event) {
		this.createdTransactionEvent = event;
	}

	public void committedTransaction(TransactionManagerEvent event) {
		this.committedTransactionEvent = event;
	}

	public void reversedTransaction(TransactionManagerEvent event) {
		this.reversedTransactionEvent = event;
	}

	public void reappliedTransaction(TransactionManagerEvent event) {
		this.reappliedTransactionEvent = event;
	}

	public void discardedTransaction(TransactionManagerEvent event) {
		this.discardedTransactionEvent = event;
	}

}
