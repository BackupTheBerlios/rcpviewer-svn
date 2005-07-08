package de.berlios.rcpviewer.gui.editors;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import de.berlios.rcpviewer.gui.fields.IFieldBuilder;
import de.berlios.rcpviewer.gui.fields.IFieldBuilder.IField;
import de.berlios.rcpviewer.gui.fields.IFieldBuilder.IFieldListener;
import de.berlios.rcpviewer.gui.jobs.SetAttributeJob;
import de.berlios.rcpviewer.gui.util.NullUtil;
import de.berlios.rcpviewer.gui.widgets.DomainObjectListener;
import de.berlios.rcpviewer.session.DomainObjectAttributeEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectListener;

/**
 * Generic form part for fields
 * @author Mike
 */
class FieldPart implements IFormPart, IFieldListener {
	
	private final IField _field;
	private final EAttribute _attribute;
	private final IDomainObjectListener _listener;
	
	private IDomainObject _object;
	private IManagedForm _managedForm;
	private boolean _isDirty= false;

	
	/**
	 * No-arg can be <code>null</code>.
	 * @param parent
	 * @param builder
	 * @param object
	 * @param attribute
	 */
	FieldPart( Composite parent, 
			   IFieldBuilder builder,
			   IDomainObject object, 
			   EAttribute attribute) {
		if ( parent == null ) throw new IllegalArgumentException();
		if ( builder == null ) throw new IllegalArgumentException();
		if ( object == null ) throw new IllegalArgumentException();
		if ( attribute == null ) throw new IllegalArgumentException();
		
		_object = object;
		_attribute = attribute;
		_field = builder.createField( parent, attribute.isChangeable(), this );
		
		// add listener that updates display whenever domain object updated
		// outside of this field
		_listener = new DomainObjectListener(){
			public void attributeChanged(DomainObjectAttributeEvent event) {
				if ( event.getEAttribute().equals( _attribute ) // JAVA_5_FIXME
						&& !NullUtil.nullSafeEquals(
								event.getNewValue(), // JAVA_5_FIXME
								_field.getGuiValue() ) ) {
					_field.setGuiValue( event.getNewValue() );
				}
			}
		};
		_object.addDomainObjectListener( _listener );	// JAVA_5_FIXME
		
	}
	
	/* IFormPart contract */
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean pOnSave) {
		if ( _attribute.isChangeable() ) {
			Job job = new SetAttributeJob( 
					_object, 
					_attribute, 
					_field.getGuiValue() );
			job.schedule();
		}
		setDirty(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		_object.removeDomainObjectListener( _listener );	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm pForm) {
		_managedForm = pForm;		
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
		return !_field.getGuiValue().equals( _object.get( _attribute ) );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		_field.setGuiValue( _object.get( _attribute ) );
		setDirty(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		_field.setFocus();		
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
	
	/* IFieldListener contract */
	
	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IFieldListener#fieldModified()
	 */
	public void fieldModified() {
		setDirty( true );
	}
	
	/* private methods */
	
	// as it says
	private void setDirty(boolean value) {
		assert _managedForm != null;
		if (_isDirty != value) {
			_isDirty= value;
			_managedForm.dirtyStateChanged();
		}
	}

}
