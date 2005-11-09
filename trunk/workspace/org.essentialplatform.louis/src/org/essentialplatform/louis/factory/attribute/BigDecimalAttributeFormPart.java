/**
 * 
 */
package org.essentialplatform.louis.factory.attribute;

import java.math.BigDecimal;

import org.eclipse.swt.widgets.Text;
import org.essentialplatform.core.domain.IDomainClass;


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
	BigDecimalAttributeFormPart( IDomainClass.IAttribute model ) {
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
