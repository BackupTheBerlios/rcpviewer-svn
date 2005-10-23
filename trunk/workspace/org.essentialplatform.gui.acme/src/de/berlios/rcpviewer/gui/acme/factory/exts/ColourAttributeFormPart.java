package de.berlios.rcpviewer.gui.acme.factory.exts;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.louis.factory.attribute.AbstractAttributeFormPart;


/**
 * @author Mike
 */
class ColourAttributeFormPart extends AbstractAttributeFormPart<Color,Text> {
	
	/**
	 * @param model
	 * @param control
	 */
	ColourAttributeFormPart( EAttribute model ) {
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
		boolean select;
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