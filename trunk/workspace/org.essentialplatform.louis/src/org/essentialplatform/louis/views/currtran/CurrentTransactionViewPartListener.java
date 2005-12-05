package org.essentialplatform.louis.views.currtran;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

/**
 * Keeps track of which parts (views and more importantly editors) are being
 * selected, and ensures that an appropriate selection listener is set up
 * on each.
 * 
 * <p>
 * Note that in order to obtain selectionChanged events, it is necessary to
 * configure the part (or rather, its site) with a selection provider.  This
 * listener does this also.
 * 
 * @author Dan Haywood
 */
public final class CurrentTransactionViewPartListener implements IPartListener2, ISelectionListener {

	private CurrentTransactionView _view;
	private ISelectionProvider _selectionProvider;

	public CurrentTransactionViewPartListener(CurrentTransactionView view, ISelectionProvider selectionProvider) {
		_view = view;
		_selectionProvider = selectionProvider;
		
		ISelectionService service = 
			_view.getSite().getWorkbenchWindow().getSelectionService();
		service.addPostSelectionListener(this);

		_view.getSite().getPage().addPartListener(this);

	}

	//////////////////////////////////////////////////////////////////////
	// ISelectionListener
	//////////////////////////////////////////////////////////////////////

	/**
	 * If the part happens to be our {@link org.essentialplatform.louis.editors.DefaultEditor}, 
	 * then set up the view inputs for the viewers in the current transaction
	 * view plugin and JUST AS IMPORTANTLY, start listening on the transaction
	 * for changes.
	 */
	public void selectionChanged(IWorkbenchPart part,
			ISelection selection) {
		_view.setupViewInputs(part);
	}

	//////////////////////////////////////////////////////////////////////
	// IPartListener2
	//////////////////////////////////////////////////////////////////////

	public void partHidden(IWorkbenchPartReference partRef) {
		_view.setVisible(false);
	}
	public void partVisible(IWorkbenchPartReference partRef) {
		_view.setVisible(true);
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
}
