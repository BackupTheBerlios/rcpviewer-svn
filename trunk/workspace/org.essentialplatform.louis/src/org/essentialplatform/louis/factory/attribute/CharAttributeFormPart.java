package org.essentialplatform.louis.factory.attribute;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Text;


/**
 * @author Mike
 */
class CharAttributeFormPart extends AbstractAttributeFormPart<Character,Text> {
	
	/**
	 * @param model
	 * @param control
	 */
	CharAttributeFormPart( EAttribute model ) {
		super( model );
	}

	@Override
	protected void displayValue(Character object, Text control) {
		if ( object == null ) {
			 control.setText( "" ); //$NON-NLS-1$
		}
		else {
			 control.setText( object.toString() );
		}
	}
}