package org.essentialplatform.louis.views.currtran;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Composite;
import org.essentialplatform.louis.views.currtran.CurrentTransactionViewContentProvider.Mode;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.louis.views.currtran.CurrentTransactionViewPartListener;

/**
 * Represents the TableViewer widgets, plus their content providers.
 * 
 * <p>
 * This saves having the other classes in the CurrentTransactionView from 
 * having to deal with the TableViewers themselves. 
 * 
 * <p>
 * In addition, acts as a SelectionProvider.  A SelectionProvider is required
 * when an ISelectionListener is installed using the SelectionService (as is
 * done in {@link CurrentTransactionViewPartListener}) in order that selection
 * change events are actually dispatched to the listeners at all.  This class
 * provides an implementation that is registered on each part as it is opened
 * such that selection changes to it are picked and propagated. 
 * 
 * @author Dan Haywood
 */
public class CurrentTransactionViewControl extends SashForm implements ISelectionProvider {

	/**
	 * The input of the TreeViewer is an IDomainObject, set up with a
	 * {@link CurrentTransactionViewContentProvider} using a mode of 
	 * {@link Mode#UNDOABLE_CHANGES}.
	 */
	private TableViewer _undoableChangesTableViewer = null;
	private CurrentTransactionViewContentProvider _undoableChangesContentProvider;

	/**
	 * The input of the TreeViewer is an IDomainObject, set up with a
	 * {@link CurrentTransactionViewContentProvider} using a mode of 
	 * {@link Mode#REDOABLE_CHANGES}.
	 */
	private TableViewer _redoableChangesTableViewer = null;
	private CurrentTransactionViewContentProvider _redoableChangesContentProvider;
	
	/**
	 * The input of the TreeViewer is an IDomainObject, set up with a
	 * {@link CurrentTransactionViewContentProvider} using a mode of 
	 * {@link Mode#ENLISTED_POJOS}.
	 */
	private TableViewer _enlistedPojosTableViewer = null;
	private CurrentTransactionViewContentProvider _enlistedPojosContentProvider;

	/**
	 * For ISelectionProvider implementation.
	 */
	private final List<ISelectionChangedListener> _listeners = 
		new ArrayList<ISelectionChangedListener>();
	
	
	
	public CurrentTransactionViewControl(final Composite parent) {
		super(parent, SWT.VERTICAL);
		
		ViewForm changesForm = new ViewForm(this, SWT.NONE);
		setTopLeft(changesForm, "Changes");
		SashForm changesSashForm = new SashForm(changesForm, SWT.HORIZONTAL);
		changesForm.setContent(changesSashForm);

		// viewers
		// the viewers' input is maintained by the workbench listener set up
		// in init(). It may be null at times.

		// undoable changes
		_undoableChangesTableViewer = 
			new TableViewer(changesSashForm, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		_undoableChangesTableViewer.setLabelProvider(new TransactionLabelProvider());
		_undoableChangesContentProvider = new CurrentTransactionViewContentProvider(Mode.UNDOABLE_CHANGES);
		_undoableChangesTableViewer.setContentProvider(_undoableChangesContentProvider);
		
		// redoable changes
		_redoableChangesTableViewer = 
			new TableViewer(changesSashForm, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		_redoableChangesTableViewer.setLabelProvider(new TransactionLabelProvider());
		_redoableChangesContentProvider = new CurrentTransactionViewContentProvider(Mode.REDOABLE_CHANGES);
		_redoableChangesTableViewer.setContentProvider(_redoableChangesContentProvider);

		// enlisted pojos
		_enlistedPojosTableViewer = 
			new TableViewer(this, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		_enlistedPojosTableViewer.setLabelProvider(new TransactionLabelProvider());
		_enlistedPojosContentProvider = new CurrentTransactionViewContentProvider(Mode.ENLISTED_POJOS);
		_enlistedPojosTableViewer.setContentProvider(_enlistedPojosContentProvider);

		setWeights(new int[]{70,30});

	}

	@Override
	public boolean setFocus() {
		return _undoableChangesTableViewer.getTable().setFocus();
	}

	
	//////////////////////////////////////////////////////////////////////
	// SelectionProvider
	//////////////////////////////////////////////////////////////////////
	
	/*
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		_listeners.add(listener);
	}

	/*
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		_listeners.remove(listener);
	}

	/*
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		if (getDomainObject() == null) {
			return StructuredSelection.EMPTY;
		}
		return new StructuredSelection(new Object[] {getDomainObject()});
	}

	/*
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		// not used
	}


	
	//////////////////////////////////////////////////////////////////////
	// Facade to viewers and their content providers
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the input of (any one of the) viewers.
	 * 
	 * <p>
	 * Since all three viewers are set up with the same input, it doesn't
	 * matter which one we obtain the input from when asked for it.
	 * 
	 * @return
	 */
	Object getInput() {
		return _undoableChangesTableViewer.getInput();
	}

	/**
	 * Returns the current transaction (if any) from (any one of the) viewers'
	 * content providers.
	 * 
	 * <p>
	 * Since all three viewers are using the same content provider (albeit 
	 * initialized in different modes to return different aspects of 
	 * information), it doesn't matter which one we obtain the transaction from 
	 * when asked for it.
	 * 
	 * @return
	 */
	ITransaction getCurrentTransaction() {
		return _undoableChangesContentProvider.getCurrentTransaction();
	}

	/**
	 * Returns the domain object from (any one of the) viewers' content 
	 * providers.
	 * 
	 * <p>
	 * Since all three viewers are using the same content provider (albeit 
	 * initialized in different modes to return different aspects of 
	 * information), it doesn't matter which one we obtain the input from 
	 * when asked for it.
	 * 
	 * @return
	 */
	IDomainObject<?> getDomainObject() {
		return _undoableChangesContentProvider.getInput();
	}

	/**
	 * Sets the input on each of the three contained table viewers.
	 * 
	 * @param domainObject
	 */
	void setInput(IDomainObject domainObject) {
		_undoableChangesTableViewer.setInput(domainObject);
		_redoableChangesTableViewer.setInput(domainObject);
		_enlistedPojosTableViewer.setInput(domainObject);
	}

	void refresh(ITransactable transactable) {
		_undoableChangesTableViewer.refresh();
		_redoableChangesTableViewer.refresh();
		_enlistedPojosTableViewer.refresh();
	}

	/////////////////////////////////////////////////////////////////////
	// Helpers
	
	private void setTopLeft(ViewForm changesForm, String text) {
		CLabel label = new CLabel(changesForm, SWT.NONE);
		label.setText(text);
		changesForm.setTopLeft(label);
	}

}
