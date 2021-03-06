package de.berlios.rcpviewer.gui.editors.parts;

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
import de.berlios.rcpviewer.session.DomainObjectAttributeEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectAttributeListener;

/**
 * Generic form part for attributes
 * @author Mike
 */
public class AttributePart implements IFormPart, IFieldListener {
	
	private final IField _field;
	private final EAttribute _attribute;
	private final IDomainObjectAttributeListener _listener;
	
	private IDomainObject<?> _object = null;
	private IManagedForm _managedForm;
	private boolean _isDirty= false;

	
	/**
	 * @param parent - cannot be <code>null</code>
	 * @param builder - cannot be <code>null</code>
	 * @param attribute - cannot be <code>null</code>
	 * @param columnWidths - can be <code>null</code>
	 */
	public AttributePart( Composite parent, 
			   IFieldBuilder builder,
			   EAttribute attribute,
			   int[] columnWidths ) {
		assert parent != null;
		assert builder != null;
		assert attribute != null;
		
		_attribute = attribute;
		// gui creation done here rather than in initialize() method
		// as this results in less state having to held onto
		_field = builder.createField( parent, attribute, this, columnWidths );
		
		// add listener that updates display whenever domain object updated
		// outside of this field
		_listener = new IDomainObjectAttributeListener(){
			public void attributeChanged(DomainObjectAttributeEvent event) {
				DomainObjectAttributeEvent<?> typedEvent 
					= (DomainObjectAttributeEvent<?>)event;
				if ( !NullUtil.nullSafeEquals(
								typedEvent.getNewValue(), 
								_field.getGuiValue() ) ) {
					_field.setGuiValue( typedEvent.getNewValue() );
				}
			}
		};
	}
	
	/* IFormPart contract */
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean pOnSave) {
		// moved down to fieldFocusLost
//		if ( _attribute.isChangeable() ) {
//			SetAttributeJob job = new SetAttributeJob( 
//					_object, 
//					_attribute, 
//					_field.getGuiValue() );
//			// REVIEW_ME: running directly, otherwise the attribute ends up being set after the xactn has committed :-(
//			// seems to work fine this way, but I know the original idea was to do everything in a job...
//			// job.schedule();
//			job.runInUIThread(null);
//		}
		setDirty(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		if ( _object != null ) {
			_object.getAttribute( _attribute ).removeListener( _listener );	
		}
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
		if ( _object == null ) {
			return _field.getGuiValue() == null;
		}
		else {
			return !_field.getGuiValue().equals( 
					_object.getAttribute( _attribute ).get() );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		if ( _object == null ) {
			_field.setGuiValue( null );
		}
		else {
			_field.setGuiValue( _object.getAttribute( _attribute ).get() );
		}
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
	public boolean setFormInput(Object input) {
		if ( input != null && !(input instanceof IDomainObject ) ) {
			throw new IllegalArgumentException();
		}
		// remove listening from old object if any
		if ( _object != null ) {
			_object.getAttribute( _attribute ).removeListener( _listener );	
		}
		_object = (IDomainObject)input;
		// add listening to new object
		if ( _object != null ) {
			_object.getAttribute( _attribute ).addListener( _listener );
		}
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
	/*
	 * @see de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder.IFieldListener#fieldFocusLost(de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder.IField)
	 */
	public void fieldFocusLost( IField field ) {
		// REVIEW_ME: moved from commit() method; seems okay for strings, haven't looked at other types though.
		if (!isStale()) {
			return;
		}
		SetAttributeJob job = new SetAttributeJob( 
				_object, 
				_attribute, 
				_field.getGuiValue() );
		job.schedule();
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
