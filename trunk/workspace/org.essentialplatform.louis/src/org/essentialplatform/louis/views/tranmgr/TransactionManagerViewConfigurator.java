/**
 * 
 */
package org.essentialplatform.louis.views.tranmgr;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.configure.IConfigurable;
import org.essentialplatform.louis.widgets.AbstractFormDisplay;
import org.essentialplatform.louis.widgets.BooleanORFilter;
import org.essentialplatform.louis.widgets.DefaultSelectionAdapter;
import org.essentialplatform.runtime.client.transaction.ITransaction;

/**
 * @author Mike
 *
 */
class TransactionManagerViewConfigurator implements IConfigurable {
	
	private final TreeViewer _viewer;
	
	/**
	 * Constructor requires viewer.
	 * @param viewer
	 */
	TransactionManagerViewConfigurator( TreeViewer viewer ) {
		assert viewer != null;
		_viewer = viewer;
	}
	
	
	
	/* IConfigurable contract */ 
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.configure.IConfigurable#run()
	 */
	public void run() {
		FilterDialog dialog = new FilterDialog();
		dialog.open();
		if ( dialog.hasChanged() ) {
			BusyIndicator.showWhile( null, new Runnable(){
				public void run() {
					applyFilters();
				}
			});
		}
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.configure.IConfigurable#addConfigurableListener(org.essentialplatform.louis.configure.IConfigurable.IConfigurableListener)
	 */
	public boolean addConfigurableListener(IConfigurableListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.configure.IConfigurable#removeConfigurableListener(org.essentialplatform.louis.configure.IConfigurable.IConfigurableListener)
	 */
	public boolean removeConfigurableListener(IConfigurableListener listener) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* package-private methods */
	
	void applyFilters() {
		try {
			_viewer.getTree().setRedraw( false );
			
			// work out which states to display
			BooleanORFilter combinedFilter = new BooleanORFilter();
			boolean includeAllStates = true;
			for ( ITransaction.State state : ITransaction.State.values() ) {
				if ( getDesiredVisibility( state ) ) {
					combinedFilter.addDelegate( new StateFilter( state ) );
				}
				else {
					includeAllStates = false;
				}
			}
			
			// apply filters
			// if showing all states, short-cut by adding no state filters
			_viewer.resetFilters();
			if ( !includeAllStates ) {
				_viewer.addFilter( combinedFilter );
			}
			if ( LouisPlugin.getDefault().getPreferenceStore().getBoolean(
					getWithEnlistedPojosKey() ) ) {
				_viewer.addFilter( new WithEnlistedPojosFilter() );
			}
		}
		finally {
			_viewer.getTree().setRedraw( true );
		}
	}
	
	
	/* private methods */
	
	// fetch the recorded visibility for the passed transaction state
	private boolean getDesiredVisibility( ITransaction.State state ) {
		return !LouisPlugin.getDefault().getPreferenceStore().getBoolean( 
				getVisibilityKey( state ) );	 
	}
	
	// set the recorded visibility for the passed transaction state
	private void setDesiredVisibility( ITransaction.State state, boolean visible ) {
		LouisPlugin.getDefault().getPreferenceStore().setValue(
				getVisibilityKey( state ), !visible );	 
	}
	
	// as it says
	private String getVisibilityKey( ITransaction.State state ) {
		assert state != null;
		StringBuffer key = new StringBuffer();
		key.append( TransactionManagerViewConfigurator.class.getSimpleName() );
		key.append( "." );
		key.append( state.toString() );
		key.append( ".Hide" );
		return key.toString() ;
	}
	
	// as it says
	private String getWithEnlistedPojosKey() {
		StringBuffer key = new StringBuffer();
		key.append( TransactionManagerViewConfigurator.class.getSimpleName() );
		key.append( ".WithEnlistedPojos" );
		return key.toString() ;
	}
	
	/* private classes */
	
	// the gui through which to configure the filters
	private class FilterDialog extends AbstractFormDisplay {
		
		private boolean _changed = false;
		
		FilterDialog() {
			super( new Shell( SWT.ON_TOP | SWT.APPLICATION_MODAL )  );
			setMinSize( new Point( 100, 100 ) );
		}

		/* (non-Javadoc)
		 * @see org.essentialplatform.gui.widgets.AbstractFormDisplay#open()
		 */
		@Override
		public int open() {
			// title
			getForm().setText( LouisPlugin.getResourceString( 
				"TransactionManagerViewConfigurator.FilterDialog.Title" ) ); //$NON-NLS-1$
			
			// main gui
			Composite body = getForm().getBody();
			body.setLayout( new GridLayout( 2, false ) );
			
			// add status visibility check boxes
			Group stateGroup = new Group( body, SWT.NONE );
			stateGroup.setLayoutData( 
					new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
			stateGroup.setLayout( new GridLayout() );
			stateGroup.setBackground( body.getBackground() );
			stateGroup.setText( LouisPlugin.getResourceString( 
				"TransactionManagerViewConfigurator.StateGroup" ) ); //$NON-NLS-1$
			for ( ITransaction.State state : ITransaction.State.values() ) {
				addStateCheckBox( stateGroup, state );
			}
			
			// other filters
			Group otherGroup = new Group( body, SWT.NONE );
			otherGroup.setLayoutData( 
					new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
			otherGroup.setLayout( new GridLayout() );
			otherGroup.setBackground( body.getBackground() );
			otherGroup.setText( LouisPlugin.getResourceString( 
				"TransactionManagerViewConfigurator.OtherGroup" ) ); //$NON-NLS-1$
			addWithEnlistedPojosCheckBox( otherGroup );
			
			// buttons
			addOKButton( ADD_DEFAULT_BEHAVIOUR );
			
			// super's functionality to open
			return super.open();
		}	
		
		// as it says
		private void addStateCheckBox( 
				Composite parent, final ITransaction.State state ) {
			assert parent != null;
			assert state != null;
			final Button button = getFormToolkit().createButton(
					parent,
					state.toString(),
					SWT.CHECK );
			button.setLayoutData( new GridData() );
			button.setSelection( getDesiredVisibility( state ) );
			button.addSelectionListener( new DefaultSelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					setDesiredVisibility( state, button.getSelection() );
					setChanged();
				}
			});
		}
		
		// as it says
		private void addWithEnlistedPojosCheckBox( Composite parent ) {
			assert parent != null;
			final Button button = getFormToolkit().createButton(
					parent,
					LouisPlugin.getResourceString( 
						"TransactionManagerViewConfigurator.WithEnlistedPojos"), //$NON-NLS-1$
					SWT.CHECK );
			button.setLayoutData( new GridData() );
			button.setSelection( 
					LouisPlugin.getDefault().getPreferenceStore().getBoolean(
							getWithEnlistedPojosKey() ) );
			button.addSelectionListener( new DefaultSelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					LouisPlugin.getDefault().getPreferenceStore().setValue(
							getWithEnlistedPojosKey(), button.getSelection() );
					setChanged();
				}
			});
		}

		boolean hasChanged() {
			return _changed;
		}
		
		private void setChanged() {
			_changed = true;
		}
	}
	
	
	// filters all transactions unless has passed status
	private class StateFilter extends ViewerFilter {
		
		private final ITransaction.State _state;
		
		StateFilter( ITransaction.State state ) {
			super();
			assert state != null;
			_state = state;
		}
		
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if( element instanceof ITransaction ) {
				return _state == ((ITransaction)element).getState();
			}
			return true;
		}
	}
	
	// filters all transactions with enlisted objects
	private class WithEnlistedPojosFilter extends ViewerFilter {
		
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if( element instanceof ITransaction ) {
				return !((ITransaction)element).getEnlistedPojos().isEmpty();
			}
			return true;
		}
	}
	
}
