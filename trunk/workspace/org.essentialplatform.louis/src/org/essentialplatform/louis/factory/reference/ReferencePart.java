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
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOneToOneReference;
import org.essentialplatform.runtime.shared.domain.event.DomainObjectReferenceEvent;
import org.essentialplatform.runtime.shared.domain.event.ExtendedDomainObjectReferenceEvent;
import org.essentialplatform.runtime.shared.domain.event.IDomainObjectReferenceListener;

class ReferencePart extends AbstractFormPart implements IConfigurable {

	private IDomainObject.IObjectOneToOneReference _model;
	
	private final IDomainClass.IReference _classReference;
	private final IDomainObjectReferenceListener _listener;
	private final IFormPart _detailsPart;
	
	private Text _control;
	private List<IReferencePartDisplayListener> _listeners = null;
	
	/**
	 * The value as displayed in the UI.
	 * 
	 * <p>
	 * This is maintained separately from the value in the backing model so
	 * that we know if there have been changes.
	 */
	private IDomainObject<?> _uiValue = null;

	
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
	
	////////////////////////////////////////////////////////////////
	// IFormPart contract 
	////////////////////////////////////////////////////////////////
	
	/*
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	public void initialize(IManagedForm form) {
		super.initialize( form );
		_detailsPart.initialize( form );
	}
	
	/*
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	@Override
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
	
	/*
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	@Override
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

	/*
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	/*
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	@Override
	public void setFocus() {
		if ( _control != null ) {
			_control.setFocus();
		}
	}

	/*
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	@Override
	public void dispose() {
		_detailsPart.dispose();
	}
	

	////////////////////////////////////////////////////////////////
	// IConfigurable contract - delegate to details
	////////////////////////////////////////////////////////////////
	
	/*
	 * @see org.essentialplatform.gui.factory.IConfigurable#run()
	 */
	public void run() {
		if ( _detailsPart instanceof IConfigurable ) {
			((IConfigurable)_detailsPart).run();
		}
	}

	/*
	 * @see org.essentialplatform.gui.factory.IConfigurable#addListener(org.essentialplatform.gui.factory.IConfigurable.IConfigurableListener)
	 */
	public boolean addConfigurableListener(IConfigurableListener listener) {
		if ( _detailsPart instanceof IConfigurable ) {
			return ((IConfigurable)_detailsPart).addConfigurableListener( listener );
		}
		return false;
	}

	/*
	 * @see org.essentialplatform.gui.factory.IConfigurable#removeListener(org.essentialplatform.gui.factory.IConfigurable.IConfigurableListener)
	 */
	public boolean removeConfigurableListener(IConfigurableListener listener) {
		if ( _detailsPart instanceof IConfigurable ) {
			return ((IConfigurable)_detailsPart).removeConfigurableListener( listener );
		}
		return false;
	}
	
	
	////////////////////////////////////////////////////////////////
	// package private methods
	////////////////////////////////////////////////////////////////
	
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
	IDomainObject<?> getUiValue() {
		return _uiValue;
	}
	
	/**
	 * Sets the value, marking the part in the UI as dirty.
	 * 
	 * <p>
	 * TODO: if the value is being reset because of an undo, then we shouldn't
	 * necessarily be set as dirty.  Indeed, if the xactn is being undone
	 * completely, then the part in the UI should no longer be marked as
	 * dirty.
	 * 
	 * @param value
	 */
	void setValue( IDomainObject<?> value  ) {

		// do nowt if no change to model value
		if ( NullUtil.nullSafeEquals( value, getUiValue() ) ) {
			return;
		}
		_uiValue = value;

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
		markDirty();
		setModelValue(value);
		_detailsPart.setFormInput( value );
		if ( _listeners != null ) {
			for ( IReferencePartDisplayListener listener : _listeners ) {
				listener.displayValueChanged( value );
			}
		}
	}

	private void setModelValue(IDomainObject<?> value) {
		if (_model != null) {
			if(!NullUtil.nullSafeEquals( value, _model.get())) {
				_model.set(value);
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
