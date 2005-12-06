package org.essentialplatform.louis.factory.reference;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.Section;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.configure.IConfigurable;
import org.essentialplatform.louis.util.NullUtil;
import org.essentialplatform.louis.util.SWTUtil;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.domain.IDomainObject.IObjectOneToOneReference;
import org.essentialplatform.runtime.domain.event.DomainObjectReferenceEvent;
import org.essentialplatform.runtime.domain.event.ExtendedDomainObjectReferenceEvent;
import org.essentialplatform.runtime.domain.event.IDomainObjectReferenceListener;

class ReferencePart extends AbstractFormPart implements IConfigurable {

	private IDomainObject.IObjectOneToOneReference _model;
	
	private final IDomainClass.IReference _classReference;
	private final IDomainObjectReferenceListener _listener;
	private final IFormPart _detailsPart;
	
	private Text _control;
	private List<IReferencePartDisplayListener> _listeners = null;
	
	/**
	 * Requires the reference
	 * @param ref
	 * @param pages
	 */
	ReferencePart( IDomainClass.IReference ref, IFormPart detailsPart ) {
		super();
		assert ref != null;
		assert detailsPart != null;
		_classReference = ref;
		_detailsPart = detailsPart;
		// add listener that updates display whenever domain object updated
		// outside of this field
		_listener = new IDomainObjectReferenceListener(){
			public void collectionAddedTo(DomainObjectReferenceEvent event) {
				// does nowt
			}
			public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
				// does nowt
			}
			public void referenceChanged(DomainObjectReferenceEvent event) {
				refresh();
			}
			public void referencePrerequisitesChanged(ExtendedDomainObjectReferenceEvent event) {
				// does nowt
			}
		};
	}
	
	/* IFormPart contract */
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm form) {
		super.initialize( form );
		_detailsPart.initialize( form );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object input) {
		try {
			// remove listening from old object if any
			if ( _model != null ) {
				_model.removeListener( _listener );	
			}
			// derive model from input and our _classReference
			if (input == null) {
				_model = null;
				return false; // not revealed any output 
			} else {
				assert input instanceof IDomainObject<?>;
				IDomainObject domainObject = (IDomainObject)input;
				_model = domainObject.getOneToOneReference( _classReference );
				// add listening to new object
				_model.addListener( _listener );
				return true;
			}
		} finally {
			refresh();	
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		IDomainObject<?> value = null;
		if ( _model != null ) {
			value = _model.get();
		} else {
			value = null;
		}
		setValue( value );
		super.refresh();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		if ( _control != null ) _control.setFocus();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		_detailsPart.dispose();
	}
	
	/* IConfigurable contract - delegate to details */
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.gui.factory.IConfigurable#run()
	 */
	public void run() {
		if ( _detailsPart instanceof IConfigurable ) {
			((IConfigurable)_detailsPart).run();
		}
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.gui.factory.IConfigurable#addListener(org.essentialplatform.gui.factory.IConfigurable.IConfigurableListener)
	 */
	public boolean addConfigurableListener(IConfigurableListener listener) {
		if ( _detailsPart instanceof IConfigurable ) {
			return ((IConfigurable)_detailsPart).addConfigurableListener( listener );
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.gui.factory.IConfigurable#removeListener(org.essentialplatform.gui.factory.IConfigurable.IConfigurableListener)
	 */
	public boolean removeConfigurableListener(IConfigurableListener listener) {
		if ( _detailsPart instanceof IConfigurable ) {
			return ((IConfigurable)_detailsPart).removeConfigurableListener( listener );
		}
		return false;
	}
	
	
	/* package private methods */
	
	/**
	 * Sets the primary control for this part.
	 * @param control
	 */
	void setControl( Text control ) {
		if ( control == null ) throw new IllegalArgumentException();
		_control = control;
	}
	
	/**
	 * Accessor to value.
	 */
	IDomainObject<?> getValue() {
		return _model.get();
	}
	
	/**
	 * Sets the value.
	 * @param value
	 */
	void setValue( IDomainObject<?> value  ) {

//		// do nowt if no change to model value
//		if ( NullUtil.nullSafeEquals( value, getValue() ) ) return;
//		_value = (T1)value;
//		markDirty();
//		if ( _control != null && updateDisplay ) {
//			displayValue( _value, _control );
//		}

		boolean valueChanged = false;
		IDomainObject<?> modelDobj = _model.get();
		if (value != null) {
			Object uiPojo = value.getPojo();
			if (modelDobj == null ||
				uiPojo != modelDobj.getPojo()) {
				valueChanged = true;
			}
		} else {
			if (modelDobj != null) {
				valueChanged = true;
			}
		}
		if (!valueChanged) {
			return;
		}
		
		_model.set(value);

		if ( _control != null ) {
			if ( value == null ) {
				_control.setText( "" ); //$NON-NLS-1$
			}
			else {
				boolean reflow = _control.getText().length() == 0;
				_control.setText( LouisPlugin.getText( value ) );
				if ( reflow ) {
					Section s = SWTUtil.getParent( _control, Section.class );
					if ( s != null ) s.layout();
				}
			}
		}
		_detailsPart.setFormInput( value );
		markDirty();
		if ( _listeners != null ) {
			for ( IReferencePartDisplayListener listener : _listeners ) {
				listener.displayValueChanged( value );
			}
		}
	}
	
	/**
	 * Adds a display value listener
	 * @param listener
	 */
	void addDisplayListener( IReferencePartDisplayListener listener ) {
		assert listener != null;
		if ( _listeners == null ) {
			_listeners = new ArrayList<IReferencePartDisplayListener>();
		}
		_listeners.add( listener );
	}


}
