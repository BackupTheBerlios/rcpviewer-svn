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
import org.essentialplatform.runtime.transaction.changes.Interaction;

/**
 * Base class for the three different content providers.
 * 
 * <p>
 * This provides much of the scaffolding for an IStructuredContentProvider 
 * (suitable for supporting tables); the ChangesContentProvider go further and
 * implement ITreeContentProvider (sub-interface of IStructuredContentProvider).
 * 
 * @author Dan Haywood
 */
abstract class AbstractCurrTranContentProvider implements IStructuredContentProvider {

	/**
	 * The input, downcast.
	 */
	private IDomainObject _input;
	/**
	 * Derived from the input (see {@link #inputChanged(Viewer, Object, Object)}).
	 */
	private ITransaction _currentTransaction;

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
		Object[] objects = doGetElements(inputElement);
		if (objects == null) return new Object[]{};
		return objects;
	}
	
	/**
	 * hook method.
	 * 
	 * @param inputElement
	 * @return
	 */
	protected abstract Object[] doGetElements(Object inputElement);

	/*
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public final void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		assert newInput instanceof IDomainObject;
		_input = (IDomainObject)newInput;
		_currentTransaction = currentTransactionIfAny();
	}

	/**
	 * The input, downcast.
	 * 
	 * @return
	 */
	public final IDomainObject getInput() {
		return _input;
	}
	
	public final ITransaction getCurrentTransaction() {
		return _currentTransaction;
	}

	/*
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public final void dispose() {
		// does nowt
	}

	//////////////////////////////////////////////////////////////////////
	// helpers
	
	/**
	 * Obtains the transaction, if any, for the input of the viewer.  
	 * @return
	 */
	protected final ITransaction currentTransactionIfAny() {
		return TransactionManager.instance().getCurrentTransactionFor(
			(ITransactable)_input.getPojo(), false);
	}

	protected final Object[] copyAsArray(Collection<?> objectList) {
		Object[] objects = new Object[objectList.size()];
		int i=0;
		for(Object o: objectList) {
			objects[i++] = o;
		}
		return objects;
	}

}
