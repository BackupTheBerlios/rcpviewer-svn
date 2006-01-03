/**
 * 
 */
package org.essentialplatform.louis.views.tranmgr;

import org.eclipse.jface.viewers.TreeViewer;
import org.essentialplatform.runtime.client.transaction.event.ITransactionManagerListener;
import org.essentialplatform.runtime.client.transaction.event.TransactionManagerEvent;
import org.essentialplatform.runtime.shared.transaction.event.ITransactionListener;
import org.essentialplatform.runtime.shared.transaction.event.TransactionEvent;

/**
 * uases passed viewer to react to changes to current transactions.
 * @author Mike
 */
class TransactionManagerViewListener implements ITransactionManagerListener {
	
	private final TreeViewer _viewer;
	private final TransactionListener _internalListener;
	
	/**
	 * Constrcutor requires parent tree.
	 * @param viewer
	 */
	TransactionManagerViewListener( TreeViewer viewer ) {
		_viewer = viewer;
		_internalListener = new TransactionListener();
	}
	

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionManagerListener#createdTransaction(org.essentialplatform.runtime.transaction.event.TransactionManagerEvent)
	 */
	public void createdTransaction(TransactionManagerEvent event) {
		event.getTransaction().addTransactionListener( _internalListener );
		_viewer.refresh();
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionManagerListener#committedTransaction(org.essentialplatform.runtime.transaction.event.TransactionManagerEvent)
	 */
	public void committedTransaction(TransactionManagerEvent event) {
		_viewer.refresh( event.getTransaction() );
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionManagerListener#reversedTransaction(org.essentialplatform.runtime.transaction.event.TransactionManagerEvent)
	 */
	public void reversedTransaction(TransactionManagerEvent event) {
		_viewer.refresh( event.getTransaction() );
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionManagerListener#reappliedTransaction(org.essentialplatform.runtime.transaction.event.TransactionManagerEvent)
	 */
	public void reappliedTransaction(TransactionManagerEvent event) {
		_viewer.refresh( event.getTransaction() );
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.runtime.transaction.event.ITransactionManagerListener#discardedTransaction(org.essentialplatform.runtime.transaction.event.TransactionManagerEvent)
	 */
	public void discardedTransaction(TransactionManagerEvent event) {
		event.getTransaction().removeTransactionListener( _internalListener );
		_viewer.refresh();
	}
	
	// for listening on individual transactions
	private class TransactionListener implements ITransactionListener {

		/*
		 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#buildingChanges(org.essentialplatform.runtime.transaction.event.TransactionEvent)
		 */
		public void buildingChanges(TransactionEvent event) {
			update(event);
		}

		/* (non-Javadoc)
		 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#addedChange(org.essentialplatform.runtime.transaction.event.TransactionEvent)
		 */
		public void addedChange(TransactionEvent event) {
			update( event );
			
		}

		/* (non-Javadoc)
		 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#committed(org.essentialplatform.runtime.transaction.event.TransactionEvent)
		 */
		public void committed(TransactionEvent event) {
			update( event );
			
		}

		/* (non-Javadoc)
		 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#discarded(org.essentialplatform.runtime.transaction.event.TransactionEvent)
		 */
		public void discarded(TransactionEvent event) {
			update( event );
			
		}

		/* (non-Javadoc)
		 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#reapplied(org.essentialplatform.runtime.transaction.event.TransactionEvent)
		 */
		public void reapplied(TransactionEvent event) {
			update( event );
			
		}

		/* (non-Javadoc)
		 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#redonePendingChange(org.essentialplatform.runtime.transaction.event.TransactionEvent)
		 */
		public void redonePendingChange(TransactionEvent event) {
			update( event );
			
		}

		/* (non-Javadoc)
		 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#redonePendingChanges(org.essentialplatform.runtime.transaction.event.TransactionEvent)
		 */
		public void redonePendingChanges(TransactionEvent event) {
			update( event );
			
		}

		/* (non-Javadoc)
		 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#reversed(org.essentialplatform.runtime.transaction.event.TransactionEvent)
		 */
		public void reversed(TransactionEvent event) {
			update( event );
			
		}

		/* (non-Javadoc)
		 * @see org.essentialplatform.runtime.transaction.event.ITransactionListener#undonePendingChange(org.essentialplatform.runtime.transaction.event.TransactionEvent)
		 */
		public void undonePendingChange(TransactionEvent event) {
			update( event );
			
		}
		

		private void update( TransactionEvent event ) {
			assert event != null;
			_viewer.refresh( event.getTransaction() );
		}

	}

}
