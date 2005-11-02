/**
 * 
 */
package org.essentialplatform.petstore.gui;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.louis.factory.attribute.AbstractAttributeFormPart;

import org.essentialplatform.petstore.domain.FilePath;

/**
 * @author Mike
 *
 */
class FilePathAttributeFormPart extends AbstractAttributeFormPart<FilePath,Text> {

	/**
	 * @param model
	 * @param control
	 */
	public FilePathAttributeFormPart( EAttribute model ) {
		super( model );
	}

	@Override
	protected void displayValue(FilePath object, Text control) {
		// object can be null
		assert control != null;
		String s;
		if ( object == null ) {
			s = ""; //$NON-NLS-1$
		}
		else {
			s = object.getPath();
		}
		control.setText( s );
	}
}
