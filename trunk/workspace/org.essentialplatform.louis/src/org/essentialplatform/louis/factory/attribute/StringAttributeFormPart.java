package org.essentialplatform.louis.factory.attribute;

import org.eclipse.swt.widgets.Text;
import org.essentialplatform.core.domain.IDomainClass;


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
	StringAttributeFormPart( IDomainClass.IAttribute model ) {
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