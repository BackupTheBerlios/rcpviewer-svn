package org.essentialplatform.louis.factory.attribute;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Text;


/**
 * @author Mike
 */
class PrimitiveAttributeFormPart<T> extends AbstractAttributeFormPart<T,Text> {

	/**
	 * @param model
	 * @param control
	 */
	PrimitiveAttributeFormPart( EAttribute model ) {
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