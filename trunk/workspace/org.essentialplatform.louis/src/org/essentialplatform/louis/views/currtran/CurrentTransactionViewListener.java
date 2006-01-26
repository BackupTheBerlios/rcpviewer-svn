/**
 * 
 */
package org.essentialplatform.louis.views.currtran;

import java.util.Set;

import org.essentialplatform.louis.views.currtran.CurrentTransactionView.AbstractCurrentTransactionViewAction;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.essentialplatform.runtime.client.transaction.event.ITransactionListener;
import org.essentialplatform.runtime.client.transaction.event.ITransactionManagerListener;
import org.essentialplatform.runtime.client.transaction.event.TransactionEvent;
import org.essentialplatform.runtime.client.transaction.event.TransactionManagerEvent;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;

/**
 * Listens for new transactions, and on a current transaction.
 * 
 * @author Dan Haywood
 */
class CurrentTransactionViewListener implements ITransactionListener, ITransactionManagerListener {
	
	private final CurrentTransactionViewControl _control;
	
	private ITransaction _transaction;
	private AbstractCurrentTransactionViewAction[] _actions;

	
	/**
	 * Installs itself as a listener on the transaction manager
	 * so that it can listen/unlisten to the current transaction for the
	 * domain object ({@link #getTransactable()}) of the corresponding viewer.
	 *  
	 * @param viewer
	 */
	public CurrentTransactionViewListener(CurrentTransactionViewControl control, AbstractCurrentTransactionViewAction... actions) {
		_control = control;
		_actions = actions;

		TransactionManager.instance().addTransactionManagerListener(this);
	}

	/**
	 * Also refreshes all actions so that they are in the correct initial state.
	 * 
	 * @param domainObject
	 */
	void startListeningOnTransactionFor(IDomainObject domainObject) {
		_control.setInput(domainObject);

		_transaction = TransactionManager.instance().getCurrentTransactionFor(domainObject.getPojo(), false);
		if (_transaction != null) {
			_transaction.addTransactionListener(this);
		}
		for(AbstractCurrentTransactionViewAction action: _actions) {
			action.setTransaction(_transaction);
		}
		for(AbstractCurrentTransactionViewAction action: _actions) {
			action.refresh();
		}
	}


	///////////////////////////////////////////////////////////////////
	// TransactionListener
	///////////////////////////////////////////////////////////////////

	/*
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#buildingChanges(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void buildingChanges(TransactionEvent event) {
		refreshViewers();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#addedChange(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void addedChange(TransactionEvent event) {
		refreshViewers();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#committed(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void committed(TransactionEvent event) {
		refreshViewers();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#discarded(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void discarded(TransactionEvent event) {
		refreshViewers();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#reapplied(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void reapplied(TransactionEvent event) {
		refreshViewers();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#redonePendingChange(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void redonePendingChange(TransactionEvent event) {
		refreshViewers();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#redonePendingChanges(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void redonePendingChanges(TransactionEvent event) {
		refreshViewers();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#reversed(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void reversed(TransactionEvent event) {
		refreshViewers();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#undonePendingChange(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void undonePendingChange(TransactionEvent event) {
		refreshViewers();
	}
	

	/**
	 * Refresh the viewer.
	 */
	private void refreshViewers() {
		_control.refresh();
		for(AbstractCurrentTransactionViewAction action: _actions) {
			action.refresh();
		}
	}
	


	///////////////////////////////////////////////////////////////////
	// TransactionManagerListener
	///////////////////////////////////////////////////////////////////
	
	public void createdTransaction(TransactionManagerEvent event) {
		startListening(event);
	}

	public void committedTransaction(TransactionManagerEvent event) {
		stopListening(event);
	}

	public void reversedTransaction(TransactionManagerEvent event) {
	}

	public void reappliedTransaction(TransactionManagerEvent event) {
	}

	public void discardedTransaction(TransactionManagerEvent event) {
	}


	@Override
	public void finalize() {
		stopListeningOnTransactionManager();
		stopListeningOnCurrentTransaction();
	}


	////////////////////////////////////////////////////////////////////
	// helper methods
	////////////////////////////////////////////////////////////////////
	
	/**
	 * An transaction event has occurred which means we should start listening 
	 * on that transaction.
	 * 
	 * @param event
	 */
	private void startListening(TransactionManagerEvent event) {
		if (getPojo() == null) {
			return;
		}
		ITransaction transaction = event.getTransaction();
		Set<IPojo> enlistedPojos = transaction.getEnlistedPojos();
		if (enlistedPojos.contains(getPojo())) {
			stopListeningOnCurrentTransaction(); // shouldn't really do anything, can't harm.
			_transaction = transaction;
			transaction.addTransactionListener(this);
		}
		refreshViewers();
	}

	/**
	 * An transaction event has occurred which means we should stop listening 
	 * on the current transaction.
	 * 
	 * @param event
	 */
	private void stopListening(TransactionManagerEvent event) {
		if (getPojo() == null) {
			return;
		}
		ITransaction transaction = event.getTransaction();
		Set<IPojo> enlistedPojos = transaction.getEnlistedPojos();
		if (enlistedPojos.contains(getPojo())) {
			stopListeningOnCurrentTransaction();
		}
	}
	
	public void stopListeningOnCurrentTransaction() {
		if (_transaction != null) {
			_transaction.removeTransactionListener(this);
		}
	}

	public void stopListeningOnTransactionManager() {
		TransactionManager.instance().removeTransactionManagerListener(this);
	}

	/**
	 * Returns the input of (any of) the viewers, cast to an ITransactable.
	 * 
	 * <p>
	 * May be null if the input of the viewer is null. 
	 * 
	 * @return
	 */
	private IPojo getPojo() {
		Object viewerInput = _control.getInput();
		if (viewerInput == null) {
			return null;
		}
		assert viewerInput instanceof IDomainObject;
		IDomainObject domainObject = (IDomainObject)viewerInput;
		
		return (IPojo)domainObject.getPojo();
	}


}
