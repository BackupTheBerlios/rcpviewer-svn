package org.essentialplatform.louis.views.tranmgr;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.configure.ConfigureWidgetFactory;
import org.essentialplatform.louis.util.ImageUtil;
import org.essentialplatform.runtime.client.transaction.ITransactionManager;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.essentialplatform.runtime.client.transaction.event.ITransactionManagerListener;

/**
 * Displays all current transactions.
 * @author Mike
 */
public class TransactionManagerView extends ViewPart {

	public static final String ID = TransactionManagerView.class.getName();

	private TreeViewer _viewer = null;

	private ITransactionManagerListener _listener = null;
	
	/**
	 * Adds actions to the view
	 * @see org.eclipse.ui.IViewPart#init(org.eclipse.ui.IViewSite)
	 */
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		

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
		parent.setLayout( new FillLayout() );
		
		// transaction manager
		ITransactionManager mgr = TransactionManager.instance();
		
		// viewer
		_viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		_viewer.setLabelProvider( new TransactionManagerViewLabelProvider() );
		_viewer.setContentProvider( new TransactionManagerViewContentProvider() );
		_viewer.setInput( mgr );

		// tie viewer to transaction manager
		_listener = new TransactionManagerViewListener( _viewer );
		mgr.addTransactionManagerListener( _listener );
		
		// apply filters
		TransactionManagerViewConfigurator config
			= new TransactionManagerViewConfigurator( _viewer );
		config.applyFilters();
		
		IAction configure = ConfigureWidgetFactory.createAction( config );
		
		IActionBars actionBars = getViewSite().getActionBars();
		actionBars.getToolBarManager().add( configure );
		actionBars.updateActionBars();
	}
	private void setupLabelAndImage(IAction action, String text, String image) {
		action.setImageDescriptor(
				ImageUtil.getImageDescriptor( 
						LouisPlugin.getDefault(), 
						image ) );
		action.setToolTipText( LouisPlugin.getResourceString( text ) );
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
