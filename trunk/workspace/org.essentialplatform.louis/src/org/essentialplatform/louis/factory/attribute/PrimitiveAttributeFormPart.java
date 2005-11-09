package org.essentialplatform.louis.factory.attribute;

import org.eclipse.swt.widgets.Text;
import org.essentialplatform.core.domain.IDomainClass;


/**
 * @author Mike
 */
class PrimitiveAttributeFormPart<T> extends AbstractAttributeFormPart<T,Text> {

	/**
	 * @param model
	 * @param control
	 */
	PrimitiveAttributeFormPart( IDomainClass.IAttribute model ) {
		super( model );
	}

	@Override
	protected void displayValue(T object, Text control) {
		String s;
		if ( object == null ) {
			control.setText( "" ); //$NON-NLS-1$
		}
		else {
			control.setText( String.valueOf( object ) );
		}
	}
}