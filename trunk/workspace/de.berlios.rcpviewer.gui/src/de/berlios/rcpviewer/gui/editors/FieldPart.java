package de.berlios.rcpviewer.gui.editors;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder;
import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder.IField;
import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder.IFieldListener;
import de.berlios.rcpviewer.gui.jobs.SetAttributeJob;
import de.berlios.rcpviewer.gui.util.NullUtil;
import de.berlios.rcpviewer.gui.widgets.DomainObjectListener;
import de.berlios.rcpviewer.session.DomainObjectAttributeEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectAttributeListener;
import de.berlios.rcpviewer.session.IDomainObjectListener;

/**
 * Generic form part for fields
 * @author Mike
 */
class FieldPart implements IFormPart, IFieldListener {
	
	private final IField _field;
	private final EAttribute _eAttribute;
	private final IDomainObject.IAttribute _domainObjectAttribute;
	private final IDomainObjectAttributeListener _listener;
	
	private IDomainObject<?> _object;
	private IManagedForm _managedForm;
	private boolean _isDirty= false;

	
	/**
	 * @param parent - cannot be <code>null</code>
	 * @param builder - cannot be <code>null</code>
	 * @param object - cannot be <code>null</code>
	 * @param attribute - cannot be <code>null</code>
	 * @param columnWidths - can be <code>null</code>
	 */
	FieldPart( Composite parent, 
			   IFieldBuilder builder,
			   IDomainObject object, 
			   EAttribute attribute,
			   int[] columnWidths ) {
		if ( parent == null ) throw new IllegalArgumentException();
		if ( builder == null ) throw new IllegalArgumentException();
		if ( object == null ) throw new IllegalArgumentException();
		if ( attribute == null ) throw new IllegalArgumentException();
		
		_object = object;
		_eAttribute = attribute;
		_domainObjectAttribute = object.getAttribute(attribute);
		_field = builder.createField( parent, attribute, this, columnWidths );
		
		// add listener that updates display whenever domain object updated
		// outside of this field
		_listener = new IDomainObjectAttributeListener(){
			public void attributeChanged(DomainObjectAttributeEvent event) {
				DomainObjectAttributeEvent<?> typedEvent 
					= (DomainObjectAttributeEvent<?>)event;
				if ( typedEvent.getEAttribute().equals( _eAttribute ) 
						&& !NullUtil.nullSafeEquals(
								typedEvent.getNewValue(), 
								_field.getGuiValue() ) ) {
					_field.setGuiValue( typedEvent.getNewValue() );
				}
			}
		};
		_domainObjectAttribute.addDomainObjectAttributeListener( _listener );
		
	}
	
	/* IFormPart contract */
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean pOnSave) {
		if ( _eAttribute.isChangeable() ) {
			Job job = new SetAttributeJob( 
					_object, 
					_eAttribute, 
					_field.getGuiValue() );
			job.schedule();
		}
		setDirty(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		_domainObjectAttribute.removeDomainObjectAttributeListener( _listener );	
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
		return !_field.getGuiValue().equals( _domainObjectAttribute.get() );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		_field.setGuiValue( _domainObjectAttribute.get(  ) );
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
	 * @see de.berlios.rcpviewer.gui.fields.IFieldBuilder.IFieldListener#fieldModified(de.berlios.rcpviewer.gui.fields.IFieldBuilder.IField)
	 */
	public void fieldModified( IField field) {
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
