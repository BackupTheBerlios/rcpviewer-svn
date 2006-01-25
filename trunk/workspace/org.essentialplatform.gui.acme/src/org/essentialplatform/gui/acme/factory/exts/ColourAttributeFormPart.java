package org.essentialplatform.gui.acme.factory.exts;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.louis.factory.attribute.AbstractAttributeFormPart;
import org.essentialplatform.core.domain.IDomainClass;


/**
 * @author Mike
 */
class ColourAttributeFormPart extends AbstractAttributeFormPart<Color,Text> {
	
	/**
	 * @param model
	 * @param control
	 */
	ColourAttributeFormPart( IDomainClass.IAttribute model ) {
		super(model );
	}

	/**
	 * @param object
	 * @param control
	 */
	@Override
	protected void displayValue(Color object, Text control) {
		// object can be null
		assert control != null;
		if ( object == null ) {
			control.setBackground( control.getParent().getBackground() );
			control.setText( "null" ); //$NON-NLS-1$
			
		}
		else {
			control.setBackground( object );
			control.setText( "" ); //$NON-NLS-1$
		}
	}

}