package org.essentialplatform.louis.factory.reference.collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.configure.ConfigurableAdapter;
import org.essentialplatform.louis.factory.reference.IReferencePartDisplayListener;
import org.essentialplatform.louis.widgets.AbstractFormDisplay;
import org.essentialplatform.louis.widgets.DefaultSelectionAdapter;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Handles dynamic behaviour for the collection gui.
 */
class CollectionTablePart extends ConfigurableAdapter 
		implements ICollectionChildPart {
	
	private final EReference _model;
	private final TableViewer _viewer;
	private final Map<EAttribute,TableColumn> _attributes;
	
	private IDomainObject<?> _container = null;
	private List<IReferencePartDisplayListener> _listeners = null;
	private CollectionPart _parent = null;
	private IManagedForm _mForm = null;

	
	/* Constructor */
	
	/**
	 * Requires the reference and the created pages.
	 * @param ref
	 * @param pages
	 */
	CollectionTablePart( EReference ref, TableViewer viewer ) {
		assert ref != null;
		_model = ref;
		assert viewer != null;
		_viewer = viewer;
		_attributes = new LinkedHashMap<EAttribute,TableColumn>();
		
		_viewer.addSelectionChangedListener( new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				if ( _listeners == null ) return;
				ISelection selected = event.getSelection();
				IDomainObject<?> dObj = null;
				if ( !selected.isEmpty() ) {
					Object first = ((IStructuredSelection)selected).getFirstElement();
					if ( first instanceof IDomainObject<?> ) {
						dObj = (IDomainObject<?>)first;
					}
				}
				for ( IReferencePartDisplayListener listener : _listeners ) {
					listener.displayValueChanged( dObj );
				}
			}
		});
		
	}
	
	
	/* ICollectionChildPart contract - most methods are dummies as handled by parent */

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm form) {
		_mForm = form;
	}

	/**
	 * Currently always false as never called.
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput( Object object ) {
		assert false;
		return false;
	}
	
	/**
	 * Never called.
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		// does nowt
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		_viewer.getControl().setFocus();
	}
	
	/**
	 * Currently always false as never queried.
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	public boolean isDirty() {
		assert false;
		return false;
	}
	

	/**
	 * Currently always false as never queried.
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		assert false;
		return false;
	}
	
	/**
	 * Never called.
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean onSave) {
		assert false;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		// does nowt
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.factory.reference.collection.ICollectionChildPart#display(java.util.List)
	 */
	public void display(List<IDomainObject<?>> display) {
		_viewer.setInput( display );
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.factory.reference.collection.ICollectionChildPart#addDisplayListener(org.essentialplatform.louis.factory.reference.IReferencePartDisplayListener)
	 */
	public void addDisplayListener(IReferencePartDisplayListener listener) {
		assert listener != null;
		if ( _listeners == null ) {
			_listeners = new ArrayList<IReferencePartDisplayListener>();
		}
		_listeners.add( listener );
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.factory.reference.collection.ICollectionChildPart#setParent(org.essentialplatform.louis.factory.reference.collection.CollectionPart)
	 */
	public void setParent(CollectionPart parent) {
		_parent = parent;
	}
	
	/* IConfigurableFormPart contract */
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		assert Display.getCurrent() != null;
		VisibilityDialog dialog = new VisibilityDialog();
		dialog.open();
		if ( dialog.hasChanged()) {
			for ( IConfigurableListener listener : getListeners() ) {
				listener.configurationChanged( this );
			}
		}	
	}
	
	
	/* package private methods */
	
	/**
	 * Adds a domain object to the collection.
	 */
	void addToCollection( IDomainObject<?> dObj ) {
		assert _parent != null;
		_parent.addToCollection( dObj );
	}
	
	/**
	 * What the viewer is displaying currentlu.
	 * @return
	 */
	List getDisplayedValues() {
		return (List)_viewer.getInput();
	}

	/**
	 * Links an attribute to the gui it is displayed on and sets initial
	 * display state.
	 * @param attribute
	 * @param area
	 */
	void addAttribute( EAttribute attribute, TableColumn column ) {
		assert attribute != null;
		assert column != null;
		_attributes.put( attribute, column );
		setVisible( column, isInitiallyVisible( attribute ) );
	}
	
	/* private methods - configuration */

	// as its says
	private void setVisible( TableColumn column, boolean visible ) {
		assert column != null;
		if ( visible && column.getWidth() == 0 ) {
			column.pack();
		}
		else if ( !visible && column.getWidth() > 0 ) {
			column.setWidth( 0 );
		}
	}
	
	// as it says
	private boolean isInitiallyVisible( EAttribute attribute ) {
		assert attribute != null;
		// eventually pick up initial settings from persistence mechanism...
		// TODO
		return true;
	}
	
	/* private classes */
	
	private class VisibilityDialog extends AbstractFormDisplay {
		
		private boolean _changed = false;
		
		VisibilityDialog() {
			super( new Shell( SWT.ON_TOP | SWT.APPLICATION_MODAL )  );
			setMinSize( new Point( 100, 100 ) );
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.widgets.AbstractFormDisplay#open()
		 */
		@Override
		public int open() {
			// title
			getForm().setText( LouisPlugin.getResourceString( 
				"CollectionTableGuiConfigurator.VisibilityDialog.Title" ) ); //$NON-NLS-1$
			
			// main gui
			Composite body = getForm().getBody();
			body.setLayout( new GridLayout() );
			
			// add visibility check boxes
			for( EAttribute attribute : _attributes.keySet() ) {
				addCheckBox( attribute.getName(),
						     body,
						     _attributes.get( attribute ) );
			}
			// spacer
			Label spacer = getFormToolkit().createLabel( body, "" ); //$NON-NLS-1$
			spacer.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );
			// check box for recording
			Button remember = getFormToolkit().createButton(
					body,
					LouisPlugin.getResourceString(
						"CollectionTableGuiConfigurator.VisibilityDialog.Remember" ),  //$NON-NLS-1$	
					SWT.CHECK );
			remember.setEnabled( false );
			
			// buttons
			addOKButton( ADD_DEFAULT_BEHAVIOUR );
			
			// super's functionality to open
			return super.open();
		}	
		
		private void addCheckBox( 
				String label, 
				Composite parent, 
				final TableColumn column ) {
			assert label != null;
			assert parent != null;
			assert column != null;
			final Button button = getFormToolkit().createButton(
					parent,
					label,
					SWT.CHECK );
			button.setLayoutData( new GridData() );
			button.setSelection( column.getWidth() > 0 );
			button.addSelectionListener( new DefaultSelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					setVisible( column, button.getSelection() );
					_changed = true;
				}
			});
		}
		
		boolean hasChanged() {
			return _changed;
		}
	}	
}