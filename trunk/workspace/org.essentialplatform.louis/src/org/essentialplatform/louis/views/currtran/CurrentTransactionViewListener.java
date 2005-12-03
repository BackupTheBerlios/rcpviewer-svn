/**
 * 
 */
package org.essentialplatform.louis.views.currtran;

import java.util.Set;

import org.eclipse.jface.viewers.TreeViewer;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.TransactionManager;
import org.essentialplatform.runtime.transaction.event.ITransactionListener;
import org.essentialplatform.runtime.transaction.event.ITransactionManagerListener;
import org.essentialplatform.runtime.transaction.event.TransactionEvent;
import org.essentialplatform.runtime.transaction.event.TransactionManagerEvent;

/**
 * 
 * Adapted from Mike's transaction manager view.
 * 
 * @author Dan Haywood
 */
class CurrentTransactionViewListener implements ITransactionListener, ITransactionManagerListener {
	
	private final TreeViewer _viewer;
	private ITransaction _transaction;
	
	/**
	 * Automatically listens to the current transaction (if any) of the 
	 * domain object represented by the viewer.
	 * 
	 * <p> 
	 * Automatically installs itself as a listener on the transaction manager
	 * so that it can listen/unlisten to the current transaction for the
	 * domain object ({@link #getTransactable()}) of the corresponding viewer.
	 *  
	 * @param viewer
	 */
	CurrentTransactionViewListener( TreeViewer viewer ) {
		_viewer = viewer;

		TransactionManager.instance().addTransactionManagerListener(this);
		
		if (getTransactable() != null) {
			_transaction =  
				TransactionManager.instance().getCurrentTransactionFor(
					getTransactable(), false);
			if (_transaction != null) {
				_transaction.addTransactionListener(this);
			}
		}
	}

	///////////////////////////////////////////////////////////////////
	// TransactionListener
	///////////////////////////////////////////////////////////////////

	/*
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#buildingChanges(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void buildingChanges(TransactionEvent event) {
		refreshViewer();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#addedChange(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void addedChange(TransactionEvent event) {
		refreshViewer();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#committed(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void committed(TransactionEvent event) {
		refreshViewer();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#discarded(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void discarded(TransactionEvent event) {
		refreshViewer();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#reapplied(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void reapplied(TransactionEvent event) {
		refreshViewer();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#redonePendingChange(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void redonePendingChange(TransactionEvent event) {
		refreshViewer();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#redonePendingChanges(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void redonePendingChanges(TransactionEvent event) {
		refreshViewer();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#reversed(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void reversed(TransactionEvent event) {
		refreshViewer();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#undonePendingChange(org.essentialplatform.runtime.transaction.event.TransactionEvent)
	 */
	public void undonePendingChange(TransactionEvent event) {
		refreshViewer();
	}
	

	/**
	 * Refresh the viewer.
	 */
	private void refreshViewer() {
		_viewer.refresh( getTransactable() );
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


	/**
	 * An transaction event has occurred which means we should start listening 
	 * on that transaction.
	 * 
	 * @param event
	 */
	private void startListening(TransactionManagerEvent event) {
		if (getTransactable() == null) {
			return;
		}
		ITransaction transaction = event.getTransaction();
		Set<ITransactable> enlistedPojos = transaction.getEnlistedPojos();
		if (enlistedPojos.contains(getTransactable())) {
			stopListeningOnCurrentTransaction(); // shouldn't really do anything, can't harm.
			_transaction = transaction;
			transaction.addTransactionListener(this);
		}
	}

	/**
	 * An transaction event has occurred which means we should stop listening 
	 * on the current transaction.
	 * 
	 * @param event
	 */
	private void stopListening(TransactionManagerEvent event) {
		if (getTransactable() == null) {
			return;
		}
		ITransaction transaction = event.getTransaction();
		Set<ITransactable> enlistedPojos = transaction.getEnlistedPojos();
		if (enlistedPojos.contains(getTransactable())) {
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
	 * Returns the input of the viewer, cast to an ITransactable.
	 * 
	 * <p>
	 * May be null if the input of the viewer is null. 
	 * 
	 * @return
	 */
	private ITransactable getTransactable() {
		Object viewerInput = _viewer.getInput();
		if (viewerInput == null) {
			return null;
		}
		assert viewerInput instanceof IDomainObject;
		IDomainObject domainObject = (IDomainObject)viewerInput;
		
		return (ITransactable) domainObject.getPojo();
	}


	public void finalize() {
		stopListeningOnTransactionManager();
		stopListeningOnCurrentTransaction();
	}

}
