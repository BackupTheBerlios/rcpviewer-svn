package org.essentialplatform.louis.factory.reference.collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ISelectionChangedListener;
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
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.configure.ConfigurableAdapter;
import org.essentialplatform.louis.widgets.AbstractFormDisplay;
import org.essentialplatform.louis.widgets.DefaultSelectionAdapter;

import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.session.DomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectReferenceListener;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.IDomainObject.ICollectionReference;

/**
 * Handles dynamic behaviour for the collection gui.
 */
class CopyOfCollectionTablePart extends ConfigurableAdapter implements IFormPart {
	
	private final EReference _model;
	private final TableViewer _viewer;
	private final IDomainObjectReferenceListener _listener;
	private final Map<EAttribute,TableColumn> _attributes;
	
	private IDomainObject<?> _container = null;
	private List<IDomainObject<?>> _displayed = null;
	private IManagedForm _mForm = null;
	private boolean _isDirty = false;
	private boolean _isStale = false;

	
	
	/* Constructor */
	
	/**
	 * Requires the reference and the created pages.
	 * @param ref
	 * @param pages
	 */
	CopyOfCollectionTablePart( EReference ref, TableViewer viewer ) {
		assert ref != null;
		_model = ref;
		assert viewer != null;
		_viewer = viewer;
		_listener = new IDomainObjectReferenceListener(){
			public void collectionAddedTo(DomainObjectReferenceEvent event) {
				refresh();
			}
			public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
				refresh();
			}
			public void referenceChanged(DomainObjectReferenceEvent event) {
				// does nowt
			}
		};
		_attributes = new LinkedHashMap<EAttribute,TableColumn>();
	}
	
	
	/* IFormPart contract */

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm form) {
		_mForm = form;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput( Object object ) {
		if ( _container != null ) {
			_container.getCollectionReference( _model ).removeListener( _listener );
			_container = null;
		}
		if ( object instanceof IDomainObject<?> ) {
			_container = (IDomainObject<?>)object;
			_container.getCollectionReference( _model ).addListener( _listener );
		}
		refresh();
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		// method can handle a null container
		setViewerInput( getCollectionDomainObjects() );
		_isStale = false;
		_isDirty = false;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		_viewer.getControl().setFocus();
	}
	
	/**
	 * Currently always false as user cannot edit records.
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	public boolean isDirty() {
		return _isDirty;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		return _isStale;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean onSave) {
		if ( onSave ) {
			assert _displayed != null;
			
			// compare displayed list with model
			ICollectionReference ref = _container.getCollectionReference( _model );
			List<IDomainObject<?>> model = getCollectionDomainObjects();
			
			// in model, not displayed - remove
			for ( IDomainObject<?> element : model ) {
				if ( !_displayed.contains( element ) ) {
					ref.removeFromCollection( element );
				}
			}
			
			// in display, not in model - add
			for ( IDomainObject<?> element : _displayed ) {
				if ( !model.contains( element ) ) {
					ref.addToCollection( element );
				}
			}
		}
		_isDirty = false;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		if ( _container != null ) {
			_container.getCollectionReference( _model ).removeListener( _listener );
		}
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
	
	
	/* other public methods */
	
	/**
	 * Listen in on changes to viewer selection
	 * @see org.eclipse.jface.viewers.Viewer#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		_viewer.addSelectionChangedListener(listener);
	}
	
	
	/* package private methods */
	
	/**
	 * Adds a domain object to the collection.
	 */
	void addToCollection( IDomainObject<?> dObj ) {
		assert dObj != null;
		assert _displayed != null;
		_displayed.add( dObj );
		setViewerInput( _displayed );
	}
	
	/**
	 * Removes a domain object from the collection.
	 */
	void removeFromCollection( IDomainObject<?> dObj ) {
		assert dObj != null;
		assert _displayed != null;
		assert _displayed.contains( dObj );
		_displayed.remove( dObj );
		setViewerInput( _displayed );
	}
	
	/**
	 * Accessor to displayed values.
	 * @return
	 */
	List<IDomainObject<?>> getDisplayedValues() {
		return _displayed;
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
	
	
	/* private methods - model handling */
	
	// set the input and marks the part dirty
	private void setViewerInput( List<IDomainObject<?>> display ) {
		assert display != null;
		_viewer.setInput( display );
		_mForm.reflow( true );
		_displayed = display;
		markDirty();
	}
	
	// returns the domain objects for all the collection's pojo's
	private List<IDomainObject<?>> getCollectionDomainObjects() {
		List<IDomainObject<?>> elements = new ArrayList<IDomainObject<?>>();
		if ( _container == null ) return elements;
		try {
			ISession session
				= RuntimePlugin.getDefault().getSessionManager().get( 
						_container.getSessionId() );
			Class<?> collectionPojoType = _model.getEType().getInstanceClass();
			for( Object pojo : _container.getCollectionReference( _model ).getCollection() ) {
				IDomainObject dElem = session.getDomainObjectFor( 
						pojo, collectionPojoType );
				assert dElem != null;
				elements.add( dElem );	
			}
		}
		catch ( CoreException ce ) {
			LouisPlugin.getDefault().getLog().log( ce.getStatus() );
		}
		return elements;
	}
	
	/**
	 * Marks the part dirty.
	 */
	private void markDirty() {
		_isDirty = true ;
		_mForm.dirtyStateChanged();
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