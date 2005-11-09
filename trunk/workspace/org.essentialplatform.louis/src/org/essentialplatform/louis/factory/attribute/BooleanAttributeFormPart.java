package org.essentialplatform.louis.factory.attribute;

import org.eclipse.swt.widgets.Button;
import org.essentialplatform.core.domain.IDomainClass;


/**
 * A form part that controls a check-box <code>Button</code>.
 * @author Mike
 */
class BooleanAttributeFormPart 
		extends AbstractAttributeFormPart<Boolean,Button> {
	
	/**
	 * @param model
	 * @param control
	 */
	BooleanAttributeFormPart( IDomainClass.IAttribute model ) {
		super(model );
	}

	/**
	 * @param object
	 * @param control
	 */
	@Override
	protected void displayValue(Boolean object, Button control) {
		// object can be null
		assert control != null;
		boolean select;
		if ( object == null ) {
			control.setSelection( false );
		}
		else {
			control.setSelection( object );
		}
	}

}