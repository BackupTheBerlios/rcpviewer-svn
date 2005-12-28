package org.essentialplatform.runtime.shared.tests.transaction;

import java.util.ArrayList;
import java.util.List;

import org.essentialplatform.runtime.shared.transaction.event.ITransactionListener;
import org.essentialplatform.runtime.shared.transaction.event.TransactionEvent;

public final class MyTransactionListener implements
		ITransactionListener {

	TransactionEvent buildingChangeEvent = null;
	TransactionEvent addedChangeEvent = null;
	TransactionEvent redonePendingChangeEvent = null;
	TransactionEvent redonePendingChangesEvent = null;
	TransactionEvent discardedEvent = null;
	TransactionEvent committedEvent = null;
	TransactionEvent reversedEvent = null;
	TransactionEvent reappliedEvent = null;

	List<TransactionEvent> undonePendingChangeEventList = new ArrayList<TransactionEvent>();
	TransactionEvent undonePendingChangeEvent = null;
	List<TransactionEvent> redonePendingChangeEventList = new ArrayList<TransactionEvent>();

	public void buildingChanges(TransactionEvent event) {
		this.buildingChangeEvent = event;
	}

	public void addedChange(TransactionEvent event) {
		this.addedChangeEvent = event;
	}

	public void undonePendingChange(TransactionEvent event) {
		this.undonePendingChangeEvent = event;
		undonePendingChangeEventList.add(event);
	}

	public void redonePendingChange(TransactionEvent event) {
		this.redonePendingChangeEvent = event;
		redonePendingChangeEventList.add(event);
	}

	public void redonePendingChanges(TransactionEvent event) {
		this.redonePendingChangesEvent = event;
	}

	public void discarded(TransactionEvent event) {
		this.discardedEvent = event;
	}

	public void committed(TransactionEvent event) {
		this.committedEvent = event;
	}

	public void reversed(TransactionEvent event) {
		this.reversedEvent = event;
	}

	public void reapplied(TransactionEvent event) {
		this.reappliedEvent = event;
	}


}
