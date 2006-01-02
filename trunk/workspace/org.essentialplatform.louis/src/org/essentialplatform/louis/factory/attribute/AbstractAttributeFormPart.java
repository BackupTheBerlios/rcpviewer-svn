package org.essentialplatform.louis.factory.attribute;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.AbstractFormPart;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.util.NullUtil;
import org.essentialplatform.runtime.client.domain.event.DomainObjectAttributeEvent;
import org.essentialplatform.runtime.client.domain.event.ExtendedDomainObjectAttributeEvent;
import org.essentialplatform.runtime.client.domain.event.IDomainObjectAttributeListener;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute;


/**
 * Base class for form parts created and returned by attribute gui factories'
 * <code>createGui</code> method
 * @author Mike
 * @param <T1> - class of the value
 * @param <T2> - class of the display control
 */
public abstract class AbstractAttributeFormPart<T1,T2 extends Control> 
		extends AbstractFormPart {

	private final IDomainClass.IAttribute _classAttribute;

	/**
	 * Derived when {@link #setFormInput(Object) is called.
	 */
	private IDomainObject.IObjectAttribute _model;

	private final IDomainObjectAttributeListener _listener;
	
	private T2 _control;
	/**
	 * The value as displayed in the UI.
	 * 
	 * <p>
	 * This is maintained separately from the value in the backing model so
	 * that we know if there have been changes.
	 */
	private T1 _uiValue = null;
	
	/**
	 * Constructor requires attribute and text field used to display the value
	 * @param model
	 * @param text
	 */
	protected AbstractAttributeFormPart( IDomainClass.IAttribute model ) {
		super();
		assert model != null;
		_classAttribute = model;
		// add listener that updates display whenever domain object updated
		// outside of this field
		_listener = new IDomainObjectAttributeListener(){
			public void attributeChanged(DomainObjectAttributeEvent event) {
				refresh();
			}
			public void attributePrerequisitesChanged(ExtendedDomainObjectAttributeEvent event) {
			}
		};
	}
	
	/**
	 * Sets the primary control for this part.
	 * @param control
	 */
	public void setControl( T2 control ) {
		if ( control == null ) throw new IllegalArgumentException();
		_control = control;
		_control.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				// does nowt.
			}

			/**
			 * Only apply changes if the value held in the UI is different
			 * from the current value held in the model.
			 */
			public void focusLost(FocusEvent e) {
				Object modelValue = _model.get();
				Object uiValue = getUiValue();
				if (modelValue == null && uiValue != null ||
					modelValue != null && !modelValue.equals(uiValue)) {
					_model.set(getUiValue());
				}
			}
		});
	}
	
	/**
	 * Accessor to main control
	 * @return
	 */
	public T2 getControl() {
		return _control;
	}
	
	////////////////////////////////////////////////////////////////
	// IFormPart contract 
	////////////////////////////////////////////////////////////////

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object input) {
		try {
			// remove listening from old object if any
			if ( _model != null ) {
				_model.removeListener( _listener );	
			}
			// derive _model from input and our _classAttribute.
			if (input == null) {
				_model = null;
				return false;  // not revealed any output
			} else {
				assert input instanceof IDomainObject<?>;
				IDomainObject domainObject = (IDomainObject)input;
				_model = domainObject.getAttribute( _classAttribute );
				// add listening to new object
				_model.addListener( _listener );
				return true;
			}
		} finally {
			refresh();
		}
	}
	
	/*
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean onSave) {
		super.commit(onSave);
	}
		
	/*
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		if ( _control != null ) {
			_control.setFocus();
		}
	}

	/*
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		Object value = null;
		if ( _model != null ) {
			value = _model.get();
		} else {
			value = null;
		}
		setValue( (T1)value, true );
		super.refresh();
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
	 * @param value - new value
	 * @param update - whether to update the display and model
	 */
	public void setValue( T1 value, boolean update ) {
		// do nowt if no change to model value
		if ( NullUtil.nullSafeEquals( value, getUiValue() ) ) {
			return;
		}
		_uiValue = (T1)value;
		markDirty();
		if (update) {
			setModelValue(value);
			if ( _control != null) {
				displayValue( _uiValue, _control );
			}
		}
	}

	private void setModelValue(T1 value) {
		if (_model != null) {
			if(!NullUtil.nullSafeEquals( value, _model.get())) {
				_model.set(value);
			}
		}
	}
	
	/**
	 * Returns the displayed value, converting blank Strings to <code>null</code>
	 * @return
	 */
	public T1 getUiValue() {
		return _uiValue;
	}

	
	/**
	 * Displays the passed value (could be <code>null</code>) in the passed control.
	 * <br>Should check whether the passed value already displayed and if so
	 * do nowt.
	 * @param object
	 * @param control
	 */
	protected abstract void displayValue( T1 object, T2 control );
}
