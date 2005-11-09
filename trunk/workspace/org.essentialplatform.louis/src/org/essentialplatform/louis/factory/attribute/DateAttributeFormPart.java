/**
 * 
 */
package org.essentialplatform.louis.factory.attribute;

import java.util.Date;

import org.eclipse.swt.widgets.Text;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.LouisPlugin;


/**
 * A form part that controls a <code>Text</code> displaying a date
 * @author Mike
 */
class DateAttributeFormPart extends AbstractAttributeFormPart<Date,Text> {

	/**
	 * @param model
	 * @param control
	 */
	DateAttributeFormPart(IDomainClass.IAttribute model) {
		super( model );
	}

	@Override
	protected void displayValue(Date object, Text control) {
		// object can be null
		assert control != null;
		String s;
		if ( object == null ) {
			s = ""; //$NON-NLS-1$
		}
		else {
			s = LouisPlugin.DATE_FORMATTER.format( object );
		}
		control.setText( s );
	}
}
