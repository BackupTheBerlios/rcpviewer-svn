package org.essentialplatform.louis.views.currtran;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.essentialplatform.louis.editors.DefaultEditor;
import org.essentialplatform.louis.util.ActionUtil;
import org.essentialplatform.louis.util.EditorUtil;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.transaction.ITransaction;
/**
 * Displays the current transaction (if any) for the selected pojo.
 * 
 * <p>
 * There are three lists shown for the transaction:
 * <ul>
 * <li> the undoable changes (ie changes that have been performed)
 * <li> the redoable changes (ie changes that have been undone)
 * <li> enlisted pojos - all pojos touched in the context of this transaction.
 * </ul>
 * There is one {@link TableViewer} for each of these lists.
 * 
 * <p>
 * The {@link CurrentTransactionViewListener} listens
 * 
 * @author Dan Haywood
 */
public class CurrentTransactionView extends ViewPart {

	private static Logger LOG = Logger.getLogger(CurrentTransactionView.class);

	public static final String ID = CurrentTransactionView.class.getName();


	/**
	 * Implements ITransactionListener
	 */
	private CurrentTransactionViewListener _listener = null;


	/**
	 * The transaction of this domain object, if any.
	 * 
	 *  <p>
	 *  Accessed by each of the content providers.
	 */
	private ITransaction _currentTransaction;
	ITransaction getCurrentTransaction() {
		return _currentTransaction;
	}
	void setCurrentTransaction(ITransaction currentTransaction) {
		_currentTransaction = currentTransaction;
	}
	
	
	private boolean _visible;
	public void setVisible(boolean visible) {
		_visible = visible;
	}
	public boolean isVisible() {
		return _visible;
	}

	private CurrentTransactionViewControl _control;
	private CurrentTransactionViewPartListener _viewPartListener;

	private UndoAction _undoAction;

	private RedoAction _redoAction;
	
	/**
	 * Adds actions to the view
	 * 
	 * @see org.eclipse.ui.IViewPart#init(org.eclipse.ui.IViewSite)
	 */
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		_visible = true;
	}

	/**
	 * Creates viewer and link it to current session via listeners.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		if (parent == null)
			throw new IllegalArgumentException();
		parent.setLayout(new FillLayout());

		// build the control; wraps the three table viewers that make up the 
		// view and also acts as an ISelectionProvider so that we can pick up
		// new editors
		_control = new CurrentTransactionViewControl(parent); 
		getSite().setSelectionProvider(_control);
		
		// track the currently selected editor (using the control as our
		// ISelectionProvider)
		_viewPartListener = new CurrentTransactionViewPartListener(this, _control);
		ISelectionService service = getSite().getWorkbenchWindow().getSelectionService();
		service.addPostSelectionListener(_viewPartListener);
		getSite().getPage().addPartListener(_viewPartListener);
		
		// add toolbar actions
		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolBarManager = actionBars.getToolBarManager();
		_undoAction = new UndoAction(toolBarManager);
		_redoAction = new RedoAction(toolBarManager);
		actionBars.updateActionBars();

		// listener to listen on transaction manager and transactions; will 
		// notify the control and also the actions if anything interesting happens
		_listener = new CurrentTransactionViewListener(_control, _undoAction, _redoAction);

		// initialize for the current editor
		// (we do this also whenever the selection changes).
		setupViewInputs(EditorUtil.getActiveEditor());

	}
	
	/**
	 * Updates the view's input with the provided part, PROVIDED that it is in
	 * fact a {@link DefaultEditor}.
	 * 
	 * <p>
	 * Any other part will be ignored.
	 */
	void setupViewInputs(IWorkbenchPart part) {
		LOG.debug("setupViewPart: " + part);

		if (!(part instanceof DefaultEditor)) {
			return;
		}
		DefaultEditor defaultEditor = (DefaultEditor) part;
		IDomainObject domainObject = defaultEditor.getDefaultEditorInput().getDomainObject();
		
		_listener.startListeningOnTransactionFor(domainObject);
	}

	/*
	 * Just delegates to the control.
	 *  
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		if (_control != null) {
			_control.setFocus();
		}
	}

	/*
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		if (_listener != null) {
			_listener.stopListeningOnCurrentTransaction();
		}
		super.dispose();
	}

	//////////////////////////////////////////////////////////////////////
	
	static abstract class AbstractCurrentTransactionViewAction extends Action {
		AbstractCurrentTransactionViewAction(IToolBarManager toolBarManager, String label, String icon) {
			toolBarManager.add(this);
			ActionUtil.setupLabelAndImage(this,
				label, "icons/org.eclipse.ui/full/etool16/" + icon); //$NON-NLS-1$
		}

		ITransaction _transaction;
		void setTransaction(ITransaction transaction) {
			_transaction = transaction;
		}

		abstract void refresh();
	}

	private static class UndoAction extends AbstractCurrentTransactionViewAction {
		UndoAction(IToolBarManager toolBarManager) {
			super(toolBarManager, "Undo", "undo_edit.gif");
		}
		@Override
		public void run() {
			if (_transaction == null) {return;}
			if (_transaction.hasUndoableChanges()) {
				_transaction.undoPendingChange();
			}
		}
		@Override
		void refresh() {
			if (_transaction == null) { return; }
			this.setEnabled(_transaction.hasUndoableChanges());
		}
	}
	
	private static class RedoAction extends AbstractCurrentTransactionViewAction {
		RedoAction(IToolBarManager toolBarManager) {
			super(toolBarManager, "Redo", "redo_edit.gif");
		}
		@Override
		public void run() {
			if (_transaction == null) {return;}
			if (_transaction.hasRedoableChanges()) {
				_transaction.redoPendingChange();
			}
		}
		@Override
		void refresh() {
			if (_transaction == null) { return; }
			this.setEnabled(_transaction.hasRedoableChanges());
		}
	}
}
