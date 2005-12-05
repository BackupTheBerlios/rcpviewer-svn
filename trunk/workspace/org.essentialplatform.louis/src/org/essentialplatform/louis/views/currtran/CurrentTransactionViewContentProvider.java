/**
 * 
 */
package org.essentialplatform.louis.views.currtran;

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.TransactionManager;
import org.essentialplatform.runtime.transaction.changes.ChangeSet;

/**
 * Returns different aspects (lists) of an in-progress transaction, depending
 * on how it has been instantiated.
 * 
 * <p>
 * The mode as passed into the constructor determines what is represented as
 * the content:
 * <ul>
 * <li> UNDOABLE_CHANGES
 * <li> REDOABLE_CHANGES
 * <li> ENLISTED_POJOS
 * </ul>
 * 
 * <p>
 * Since there will be three instances, each maintains its own reference to
 * the current transaction.  Not sure if this violates the DRY principle or not;
 * at any rate, haven't tried refactoring. 
 * 
 * @author Dan Haywood
 */
class CurrentTransactionViewContentProvider implements IStructuredContentProvider {

	static enum Mode {
		UNDOABLE_CHANGES,
		REDOABLE_CHANGES,
		ENLISTED_POJOS
	}
	
	/**
	 * The input, downcast.
	 */
	private IDomainObject _input;
	/**
	 * Derived from the input (see {@link #inputChanged(Viewer, Object, Object)}).
	 */
	private ITransaction _currentTransaction;

	/**
	 * The mode in which this content provider has been instantiated; used in
	 * {@link #getElements(Object)}.
	 */
	private final Mode _mode;
	
	CurrentTransactionViewContentProvider(final Mode mode) {
		_mode = mode;
	}

	/* 
	 * Used to obtain the elements of root (should be the current transaction).
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		assert inputElement != null && 
		       inputElement instanceof ITransaction;
		if (_currentTransaction == null) {
			return new Object[]{};
		}
		if (_mode == Mode.UNDOABLE_CHANGES) {
			return copyAsArray(_currentTransaction.getUndoableChanges());	
		} else
		if (_mode == Mode.REDOABLE_CHANGES) {
			return copyAsArray(_currentTransaction.getRedoableChanges());	
		} else
		if (_mode == Mode.ENLISTED_POJOS) {
			return copyAsArray(_currentTransaction.getEnlistedPojos());	
		}
		return new Object[]{};
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
	
	ITransaction getCurrentTransaction() {
		return _currentTransaction;
	}

	/*
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// does nowt
	}

	//////////////////////////////////////////////////////////////////////
	// helpers
	
	/**
	 * Obtains the transaction, if any, for the input of the viewer.  
	 * @return
	 */
	private ITransaction currentTransactionIfAny() {
		return TransactionManager.instance().getCurrentTransactionFor(
			(ITransactable)_input.getPojo());
	}

	private Object[] copyAsArray(Collection<?> objectList) {
		Object[] objects = new Object[objectList.size()];
		int i=0;
		for(Object o: objectList) {
			objects[i++] = o;
		}
		return objects;
	}

}
