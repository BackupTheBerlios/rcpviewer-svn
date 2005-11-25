/**
 * 
 */
package org.essentialplatform.louis.views.transactiontree;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.essentialplatform.runtime.transaction.ITransactionManager;

/**
 * @author Mike
 */
class TransactionTreeContentProvider implements ITreeContentProvider {


	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object element) {
//		assert element instanceof ITransaction;
//		List combined = new ArrayList();
//		combined.addAll( ((ITransaction)element).getEnlistedPojos() );
//		combined.addAll( ((ITransaction)element).getRedoableChanges() );
//		return combined.toArray();
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
//		if ( element instanceof ITransaction ) {
//			if ( !((ITransaction)element).getEnlistedPojos().isEmpty() ) return true;
//			if ( !((ITransaction)element).getRedoableChanges().isEmpty() ) return true;
//		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		assert inputElement != null;
		assert inputElement instanceof ITransactionManager;
		return ((ITransactionManager)inputElement).getCurrentTransactions().toArray();
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
		// does nowt
	}

}
