package org.essentialplatform.louis.views.currtran;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.essentialplatform.runtime.domain.IDomainObject;

/**
 * Allows the current transaction view to watch for changes to
 * the current workbench part.
 * 
 * @author Dan Haywood
 */
public class CurrentTransactionViewSelectionProvider implements
		ISelectionProvider {

	private final List<ISelectionChangedListener> _listeners = 
		new ArrayList<ISelectionChangedListener>();
	
	private final CurrentTransactionViewContentProvider _contentProvider;

	public CurrentTransactionViewSelectionProvider(CurrentTransactionViewContentProvider contentProvider) {
		_contentProvider = contentProvider;
	}
	
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		_listeners.add(listener);
	}

	public ISelection getSelection() {
		IDomainObject contentProviderInput = _contentProvider.getInput();
		if (contentProviderInput == null) {
			return StructuredSelection.EMPTY;
		}
		return new StructuredSelection(new Object[] {contentProviderInput});
	}

	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		_listeners.remove(listener);
	}

	
	public void setSelection(ISelection selection) {
		// not used
	}

}
