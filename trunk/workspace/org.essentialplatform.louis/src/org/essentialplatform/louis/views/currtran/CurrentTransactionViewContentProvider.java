/**
 * 
 */
package org.essentialplatform.louis.views.currtran;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.TransactionManager;
import org.essentialplatform.runtime.transaction.changes.ChangeSet;

/**
 * Adapted from Mike's transaction manager view.
 * 
 * @author Dan Haywood
 */
class CurrentTransactionViewContentProvider implements ITreeContentProvider {

	/**
	 * Represents the node in the tree that holds the collection of
	 * undo change sets.
	 * 
	 * @author Dan Haywood
	 */
	private static class UndoableChanges {
		@Override
		public String toString() { return "Undoable Changes"; }
	}
	
	/**
	 * Represents the node in the tree that holds the collection of
	 * redo change sets.
	 * 
	 * @author Dan Haywood
	 */
	private static class RedoableChanges {
		@Override
		public String toString() { return "Redoable Changes"; }
	}

	/**
	 * The input, downcast.
	 */
	private IDomainObject _input;

	/**
	 * The transaction of this domain object, if any. 
	 */
	private ITransaction _currentTransaction;
	
	/**
	 * Placeholder when traversing the tree, to hold the undoable changes of
	 * the current transaction.
	 * 
	 * <p>
	 * Note that it isn't necessary to instantiate a new object whenever the
	 * input (transaction) changes.
	 */
	private Object _undoableChanges = new UndoableChanges();
	/**
	 * Placeholder when traversing the tree, to hold the redoable changes of
	 * the current transaction.
	 * 
	 * <p>
	 * Note that it isn't necessary to instantiate a new object whenever the
	 * input (transaction) changes.
	 */
	private Object _redoableChanges = new RedoableChanges();


	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object element) {
		// root element
		if (element instanceof ITransaction) {
			return getElements(element);
		}
		// the transaction's undoable changes
		if (element instanceof UndoableChanges && _currentTransaction != null) {
			return _currentTransaction.getUndoableChanges().toArray();
		}
		// the transaction's redoable changes
		if (element instanceof RedoableChanges && _currentTransaction != null) {
			return _currentTransaction.getRedoableChanges().toArray();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		// root element
		if (element instanceof ITransaction) {
			return null;
		}
		// the transaction's undoable changes
		if (element instanceof UndoableChanges) {
			return _input;
		}
		// the transaction's redoable changes
		if (element instanceof RedoableChanges) {
			return _input;
		}
		// change sets
		if (element instanceof ChangeSet) {
			ChangeSet changeSet = (ChangeSet)element;
			if (_currentTransaction.getUndoableChanges().contains(changeSet)) {
				return _undoableChanges;
			}
			if (_currentTransaction.getRedoableChanges().contains(changeSet)) {
				return _redoableChanges;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		if (element instanceof ITransaction) {
			// have the undoableChanges and redoableChanges nodes
			return true;
		}
		// the transaction's undoable changes
		if (element instanceof UndoableChanges) {
			return _currentTransaction.getUndoableChanges().size() > 0;
		}
		// the transaction's redoable changes
		if (element instanceof RedoableChanges) {
			return _currentTransaction.getRedoableChanges().size() > 0;
		}
		return false;
	}

	/* 
	 * Used to obtain the elements of root (should be the current transaction).
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		assert inputElement != null && 
		       inputElement instanceof ITransaction;
		return new Object[]{_undoableChanges, _redoableChanges};
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
		assert newInput instanceof IDomainObject;
		_input = (IDomainObject)newInput;
		_currentTransaction = currentTransactionIfAny();
	}

	/**
	 * The input, downcast.
	 * 
	 * @return
	 */
	public IDomainObject getInput() {
		return _input;
	}

	/**
	 * Obtains the transaction, if any, for the input of the viewer.  
	 * @return
	 */
	private ITransaction currentTransactionIfAny() {
		return TransactionManager.instance().getCurrentTransactionFor(
			(ITransactable)_input.getPojo());
	}

}
