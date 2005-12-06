package org.essentialplatform.louis.views.currtran;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.louis.views.currtran.CurrentTransactionViewPartListener;
import org.essentialplatform.louis.views.currtran.ChangesContentProvider.Mode;

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
	 * {@link AbstractCurrTranContentProvider} using a mode of 
	 * {@link Mode#UNDOABLE_CHANGES}.
	 */
	private TreeViewer _undoableChangesTreeViewer = null;
	private AbstractCurrTranContentProvider _undoableChangesContentProvider;

	/**
	 * The input of the TreeViewer is an IDomainObject, set up with a
	 * {@link AbstractCurrTranContentProvider} using a mode of 
	 * {@link Mode#REDOABLE_CHANGES}.
	 */
	private TreeViewer _redoableChangesTreeViewer = null;
	private AbstractCurrTranContentProvider _redoableChangesContentProvider;
	
	/**
	 * The input of the TreeViewer is an IDomainObject, set up with a
	 * {@link AbstractCurrTranContentProvider} using a mode of 
	 * {@link Mode#ENLISTED_POJOS}.
	 */
	private TableViewer _enlistedPojosTreeViewer = null;
	private AbstractCurrTranContentProvider _enlistedPojosContentProvider;

	/**
	 * For ISelectionProvider implementation.
	 */
	private final List<ISelectionChangedListener> _listeners = 
		new ArrayList<ISelectionChangedListener>();
	
	
	
	public CurrentTransactionViewControl(final Composite parent) {
		super(parent, SWT.VERTICAL);

		// divide composite into two, for changes (a sash) and pojos...
		SashForm changesSashForm = new SashForm(this, SWT.HORIZONTAL);
		ViewForm enlistedPojosForm = new ViewForm(this, SWT.HORIZONTAL);
		setTopLeft(enlistedPojosForm, "Enlisted objects");

		setWeights(new int[]{70,30});

		// ... and then split the upper changes sash into two 
		// for undoable and redoable.
		ViewForm redoableChangesForm = new ViewForm(changesSashForm, SWT.NONE);
		setTopLeft(redoableChangesForm, "Redoable changes");
		
		ViewForm undoableChangesForm = new ViewForm(changesSashForm, SWT.NONE);
		setTopLeft(undoableChangesForm, "Undoable changes");

		
		// now attach the viewers to each of the view forms defined above
		
		// undoable changes
		_undoableChangesTreeViewer = 
			new TreeViewer(undoableChangesForm, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		undoableChangesForm.setContent(_undoableChangesTreeViewer.getControl());
		_undoableChangesTreeViewer.setLabelProvider(new TransactionLabelProvider());
		_undoableChangesContentProvider = new ChangesContentProvider(Mode.UNDOABLE_CHANGES);
		_undoableChangesTreeViewer.setContentProvider(_undoableChangesContentProvider);
		
		// redoable changes
		_redoableChangesTreeViewer = 
			new TreeViewer(redoableChangesForm, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		redoableChangesForm.setContent(_redoableChangesTreeViewer.getControl());
		_redoableChangesTreeViewer.setLabelProvider(new TransactionLabelProvider());
		_redoableChangesContentProvider = new ChangesContentProvider(Mode.REDOABLE_CHANGES);
		_redoableChangesTreeViewer.setContentProvider(_redoableChangesContentProvider);

		// enlisted pojos
		_enlistedPojosTreeViewer = 
			new TableViewer(enlistedPojosForm, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		enlistedPojosForm.setContent(_enlistedPojosTreeViewer.getControl());
		_enlistedPojosTreeViewer.setLabelProvider(new EnlistedPojosLabelProvider());
		_enlistedPojosContentProvider = new EnlistedPojosContentProvider();
		_enlistedPojosTreeViewer.setContentProvider(_enlistedPojosContentProvider);

	}

	@Override
	public boolean setFocus() {
		return _undoableChangesTreeViewer.getTree().setFocus();
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
		return _undoableChangesTreeViewer.getInput();
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
		_undoableChangesTreeViewer.setInput(domainObject);
		_redoableChangesTreeViewer.setInput(domainObject);
		_enlistedPojosTreeViewer.setInput(domainObject);
	}

	void refresh() {
		_undoableChangesTreeViewer.refresh();
		_redoableChangesTreeViewer.refresh();
		_enlistedPojosTreeViewer.refresh();
	}

	/////////////////////////////////////////////////////////////////////
	// Helpers
	
	private void setTopLeft(ViewForm viewForm, String text) {
		CLabel label = new CLabel(viewForm, SWT.NONE);
		label.setText(text);
		viewForm.setTopLeft(label);
	}

}
