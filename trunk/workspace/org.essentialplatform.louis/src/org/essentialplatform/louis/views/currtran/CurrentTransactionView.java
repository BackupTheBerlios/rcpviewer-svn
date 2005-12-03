package org.essentialplatform.louis.views.currtran;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
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

/**
 * Displays the current transaction (if any) for the selected pojo.
 * 
 * @author Dan Haywood
 */
public class CurrentTransactionView extends ViewPart {

	private static Logger LOG = Logger.getLogger(CurrentTransactionView.class);

	public static final String ID = CurrentTransactionView.class.getName();

	/**
	 * The input of the TreeViewer is an IDomainObject.
	 */
	private TreeViewer _viewer = null;

	/**
	 * Implements ITransactionListener
	 */
	private CurrentTransactionViewListener _listener = null;

	private boolean _visible;

	private CurrentTransactionViewContentProvider _contentProvider;

	private CurrentTransactionViewSelectionProvider _selectionProvider;
	
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
	 * Updates the view's input with the provided part, PROVIDED that it is in
	 * fact a {@link DefaultEditor}.
	 * 
	 * <p>
	 * Any other part will be ignored.
	 */
	private void setupViewInput(IWorkbenchPart part) {
		LOG.debug("setupViewPart: " + part);
		if (!(part instanceof DefaultEditor)) {
			return;
		}
		DefaultEditor defaultEditor = (DefaultEditor) part;
		_viewer.setInput(defaultEditor.getDefaultEditorInput()
				.getDomainObject());

		// tie viewer to transaction manager
		_listener = new CurrentTransactionViewListener(_viewer);
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

		// viewer
		// the viewer's input is maintained by the workbench listener set up
		// in init(). It may be null at times.
		_viewer = new TreeViewer(
				parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		_viewer.setLabelProvider(new CurrentTransactionViewLabelProvider());
		_contentProvider = new CurrentTransactionViewContentProvider();
		_viewer.setContentProvider(_contentProvider);
		setupViewInput(EditorUtil.getActiveEditor());

		// apply filters
		CurrentTransactionViewConfigurator config = new CurrentTransactionViewConfigurator(
				_viewer);
		config.applyPreferences();

		// now have viewer can add toolbar actions
		IAction undoAction = new Action() {
			@Override
			public void run() {
				DialogUtil.showNotImplementedDialog();
			}
		};
		ActionUtil.setupLabelAndImage(undoAction,
				"Undo", "icons/org.eclipse.ui/full/etool16/undo_edit.gif"); //$NON-NLS-1$

		IAction redoAction = new Action() {
			@Override
			public void run() {
				DialogUtil.showNotImplementedDialog();
			}
		};
		ActionUtil.setupLabelAndImage(redoAction,
				"Redo", "icons/org.eclipse.ui/full/etool16/redo_edit.gif"); //$NON-NLS-1$ 

		IAction configure = ConfigureWidgetFactory.createAction(config);

		IActionBars actionBars = getViewSite().getActionBars();
		actionBars.getToolBarManager().add(undoAction);
		actionBars.getToolBarManager().add(redoAction);
		actionBars.getToolBarManager().add(configure);
		actionBars.updateActionBars();

		_selectionProvider = new CurrentTransactionViewSelectionProvider(_contentProvider);
		getSite().setSelectionProvider(_selectionProvider);
		
		
		
		// track the currently selected editor.
		ISelectionService service = getSite().getWorkbenchWindow().getSelectionService();
		ISelectionListener selectionListener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart part,
					ISelection selection) {
				setupViewInput(part);
			}
		};
		service.addPostSelectionListener(selectionListener);

		// Track whether this view is visible or not.
		IPartListener2 partListener = new IPartListener2() {
			public void partHidden(IWorkbenchPartReference partRef) {
				_visible = false;
			}

			public void partVisible(IWorkbenchPartReference partRef) {
				_visible = true;
			}

			public void partActivated(IWorkbenchPartReference partRef) {
				// do nothing
			}

			public void partDeactivated(IWorkbenchPartReference partRef) {
				// do nothing
			}

			public void partBroughtToTop(IWorkbenchPartReference partRef) {
				// do nothing
			}

			/**
			 * make sure that the site that holds the part referenced in this
			 * event has a selection provider and thus when it is selected
			 * our selection listener will be notified.
			 */
			public void partOpened(IWorkbenchPartReference partRef) {
				partRef.getPart(false).getSite().setSelectionProvider(_selectionProvider);
			}

			/**
			 * Teardown the selection provider when the part closes; we don't
			 * need to listen any more.
			 */
			public void partClosed(IWorkbenchPartReference partRef) {
				partRef.getPart(false).getSite().setSelectionProvider(null);
			}

			public void partInputChanged(IWorkbenchPartReference partRef) {
				// do nothing
			}
		};
		getSite().getPage().addPartListener(partListener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		if (_viewer != null) {
			_viewer.getTree().setFocus();
		}
	}

	/*
	 * (non-Javadoc)
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
}
