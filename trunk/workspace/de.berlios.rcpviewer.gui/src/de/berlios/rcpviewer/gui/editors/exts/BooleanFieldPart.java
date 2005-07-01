package de.berlios.rcpviewer.gui.editors.exts;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import de.berlios.rcpviewer.gui.jobs.SetAttributeJob;
import de.berlios.rcpviewer.gui.widgets.DefaultSelectionAdapter;
import de.berlios.rcpviewer.session.IDomainObject;


/**
 * Generates a checkbox.
 * @author Mike
 */
class BooleanFieldPart implements IFormPart {
	
	private final Button _button;
	private final EAttribute _attribute;
	
	private IDomainObject _object;
	private IManagedForm _managedForm;
	private boolean _isDirty= false;

	
	/**
	 * @param parent
	 * @param object
	 * @param attribute
	 */
	BooleanFieldPart( Composite parent, 
					  IDomainObject object, 
					  EAttribute attribute) {
		assert parent != null;
		assert object != null;
		assert attribute != null;
		
		_object = object;
		_attribute = attribute;
		
		parent.setLayout( new GridLayout() );
		_button= new Button( parent, SWT.CHECK );
		_button.setLayoutData( new GridData() );
		_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent pE) {
				setDirty(true);
			}
		});
		
		if ( attribute.isChangeable() ) {
			_button.addSelectionListener( new DefaultSelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent pE) {
					setDirty(true);
				}
			});
		}
		else {
			_button.setEnabled( false );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean pOnSave) {
		if ( _attribute.isChangeable() ) {
			new SetAttributeJob( _object, _attribute, _button.getSelection() ).schedule();
		}
		setDirty(false);
	}

	/**
	 * @param value
	 */
	private void setDirty(boolean value) {
		if (_isDirty != value) {
			_isDirty= value;
			_managedForm.dirtyStateChanged();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		// do nothing		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm pForm) {
		_managedForm= pForm;		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	public boolean isDirty() {
		return _isDirty;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		_button.setSelection( (Boolean)_object.get( _attribute ) );
		setDirty(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		_button.setFocus();		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object pInput) {
		if ( !(pInput instanceof IDomainObject ) ) {
			throw new IllegalArgumentException();
		}
		_object = (IDomainObject)pInput;
		refresh();
		return true;		
	}

}
