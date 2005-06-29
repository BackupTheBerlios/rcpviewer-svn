package de.berlios.rcpviewer.gui.editors;

import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

/**
 * Type for building individual fields within a gui.
 * @author Mike
 */
public interface IFieldBuilder {
	
	public static final String EXTENSION_POINT_ID
		= "de.berlios.rcpviewer.gui.fieldbuilder";
	
	public boolean isApplicable( Class clazz, Object value );

	//REVIEW_CHANGE for mike: create a managed for part as well as the UI.  Needed in order to manage dirty state.
	public IFormPart createFormPart( Composite parent, Method getMethod, Method setMethod, Object configuration);
}
