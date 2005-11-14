package org.essentialplatform.louis.factory.attribute;

import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.AbstractFormPart;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.util.NullUtil;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IDomainObject.IObjectAttribute;
import org.essentialplatform.runtime.domain.event.DomainObjectAttributeEvent;
import org.essentialplatform.runtime.domain.event.ExtendedDomainObjectAttributeEvent;
import org.essentialplatform.runtime.domain.event.IDomainObjectAttributeListener;


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
	private T1 _value = null;
	
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
	}
	
	/**
	 * Accessor to main control
	 * @return
	 */
	public T2 getControl() {
		return _control;
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		Object value = null;
		if ( _model != null ) {
			value = _model.get();
		}
		setValue( (T1)value, true );
		super.refresh();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		if ( _control != null ) _control.setFocus();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean onSave) {
		if ( onSave ) {
			_model.set( getValue() );
		}
		super.commit(onSave);
	}
		
	/**
	 * Sets the value, marking the field as dirty.
	 * @param value - new value
	 * @param update - whether to update the display or not
	 */
	public void setValue( T1 value, boolean updateDisplay ) {
		// do nowt if no change to model value
		if ( NullUtil.nullSafeEquals( value, getValue() ) ) return;
		_value = (T1)value;
		markDirty();
		if ( _control != null && updateDisplay ) {
			displayValue( _value, _control );
		}
	}
	
	/**
	 * Returns the displayed value, converting blank Strings to <code>null</code>
	 * @return
	 */
	public T1 getValue() {
		return _value;
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
