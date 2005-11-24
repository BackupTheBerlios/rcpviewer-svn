package org.essentialplatform.louis.views.transactiontree;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.essentialplatform.runtime.transaction.ITransactionManager;
import org.essentialplatform.runtime.transaction.TransactionManager;
import org.essentialplatform.runtime.transaction.event.ITransactionManagerListener;

/**
 * Displays all current transactions.
 * @author Mike
 */
public class TransactionTreeView extends ViewPart {

	public static final String ID = TransactionTreeView.class.getName();

	private TreeViewer _viewer = null;

	private ITransactionManagerListener _listener = null;

	/**
	 * Creates viewer and link it to current session via listeners.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		if (parent == null)
			throw new IllegalArgumentException();
		parent.setLayout( new FillLayout() );
		
		// transaction manager
		ITransactionManager mgr = TransactionManager.instance();
		
		// viewer
		_viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		_viewer.setLabelProvider( new TransactionTreeLabelProvider() );
		_viewer.setContentProvider( new TransactionTreeContentProvider() );
		_viewer.setInput( mgr );

		// tie viewer to transaction manager
		_listener = new TransactionTreeListener( _viewer );
		mgr.addTransactionManagerListener( _listener );
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
		if ( _listener != null ) {
			TransactionManager.instance().removeTransactionManagerListener( _listener );
		}
		super.dispose();
	}
}
