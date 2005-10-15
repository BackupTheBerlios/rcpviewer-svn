/**
 * 
 */
package org.essentialplatform.louis.factory.attribute;

import java.math.BigDecimal;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Text;


/**
 * A form part that controls a <code>Text</code> displaying a 
 * <code>BigDecimal</code>
 * @author Mike
 */
class BigDecimalAttributeFormPart extends AbstractAttributeFormPart<BigDecimal,Text> {

	/**
	 * @param model
	 * @param control
	 */
	BigDecimalAttributeFormPart( EAttribute model ) {
		super( model );
	}

	@Override
	protected void displayValue(BigDecimal object, Text control) {
		if ( object == null ) {
			control.setText( "" ); //$NON-NLS-1$
		}
		else {
			control.setText( object.toString() );
		}
	}
}
