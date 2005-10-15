package org.essentialplatform.louis.factory.attribute;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Text;


/**
 * The form part that controls a <code>Text</code> field
 * @author Mike
 *
 * @param <T>
 */
class StringAttributeFormPart extends AbstractAttributeFormPart<String,Text> {
	
	/**
	 * @param model
	 * @param control
	 */
	StringAttributeFormPart( EAttribute model ) {
		super( model );
	}

	@Override
	protected void displayValue(String object, Text control) {
		String s;
		if ( object == null ) {
			 control.setText( "" ); //$NON-NLS-1$
		}
		else {
			 control.setText( object );
		}
	}
}