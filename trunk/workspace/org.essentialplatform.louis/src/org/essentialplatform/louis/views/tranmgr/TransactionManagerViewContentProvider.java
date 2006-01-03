/**
 * 
 */
package org.essentialplatform.louis.views.tranmgr;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.essentialplatform.runtime.client.transaction.ITransactionManager;
import org.essentialplatform.runtime.shared.transaction.ITransaction;
import org.essentialplatform.runtime.shared.transaction.changes.Interaction;

/**
 * @author Mike
 */
class TransactionManagerViewContentProvider implements ITreeContentProvider {

	/**
	 * The transaction manager itself.
	 */
	private Object _currentInput;

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object element) {
		// root element
		if (element instanceof ITransactionManager) {
			return getElements(element);
		}
		// transactions
		if (element instanceof ITransaction) {
			ITransaction transaction = (ITransaction)element;
			return transaction.getEnlistedPojos().toArray();
		}
		// change sets (have no children)
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		// root element
		if (element instanceof ITransactionManager) {
			return null;
		}
		// transactions
		if (element instanceof ITransaction) {
			return _currentInput;
		}
		// change sets
		if (element instanceof Interaction) {
			Interaction interaction = (Interaction)element;
			return interaction.getTransaction();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		if (element instanceof ITransactionManager) {
			ITransactionManager transactionManager = (ITransactionManager)element;
			return !transactionManager.getCurrentTransactions().isEmpty();
		}
		if (element instanceof ITransaction) {
			ITransaction transaction = (ITransaction)element;
			return !transaction.getEnlistedPojos().isEmpty();
		}
		return false;
	}

	/* 
	 * Used to obtain the elements of root (should be the transaction manager).
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		assert inputElement != null && 
		       inputElement instanceof ITransactionManager;
		ITransactionManager transactionManager = ((ITransactionManager)inputElement);
		return transactionManager.getCurrentTransactions().toArray();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// does nowt
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		_currentInput = newInput;
	}

}
