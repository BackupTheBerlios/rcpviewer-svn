package org.essentialplatform.louis.views.currtran;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.essentialplatform.louis.configure.ConfigureWidgetFactory;
import org.essentialplatform.louis.editors.DefaultEditor;
import org.essentialplatform.louis.util.ActionUtil;
import org.essentialplatform.louis.util.DialogUtil;
import org.essentialplatform.louis.util.EditorUtil;
import org.essentialplatform.louis.views.currtran.CurrentTransactionViewContentProvider.Mode;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.TransactionManager;
import org.eclipse.jface.viewers.TableViewer;
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

		// wraps the three table viewers that make up the view.
		_control = new CurrentTransactionViewControl(parent); 
		
		// tie viewer to transaction manager; it will notify the control
		// if anything interesting happens
		_listener = new CurrentTransactionViewListener(_control);

		// initialize for the current editor
		// (we do this also whenever the selection changes).
		setupViewInputs(EditorUtil.getActiveEditor());
		
		// add toolbar actions
		setupActionBar();

		getSite().setSelectionProvider(_control);
		
		_viewPartListener = new CurrentTransactionViewPartListener(this, _control);
		
		// track the currently selected editor.
		ISelectionService service = getSite().getWorkbenchWindow().getSelectionService();
		service.addPostSelectionListener(_viewPartListener);
		getSite().getPage().addPartListener(_viewPartListener);

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
	
	private void setupActionBar() {
		IAction undoAction = new Action() {
			@Override
			public void run() {
				ITransaction transaction = _control.getCurrentTransaction();
				if (transaction == null) {return;}
				if (transaction.hasUndoableChanges()) {
					transaction.undoPendingChange();
				}
			}
		};
		ActionUtil.setupLabelAndImage(undoAction,
				"Undo", "icons/org.eclipse.ui/full/etool16/undo_edit.gif"); //$NON-NLS-1$

		IAction redoAction = new Action() {
			@Override
			public void run() {
				ITransaction transaction = _control.getCurrentTransaction();
				if (transaction == null) {return;}
				if (transaction.hasRedoableChanges()) {
					transaction.undoPendingChange();
				}
			}
		};
		ActionUtil.setupLabelAndImage(redoAction,
				"Redo", "icons/org.eclipse.ui/full/etool16/redo_edit.gif"); //$NON-NLS-1$ 

		IActionBars actionBars = getViewSite().getActionBars();
		actionBars.getToolBarManager().add(undoAction);
		actionBars.getToolBarManager().add(redoAction);
		actionBars.updateActionBars();
	}


}
