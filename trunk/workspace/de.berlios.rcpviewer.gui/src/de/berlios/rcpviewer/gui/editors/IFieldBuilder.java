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

	/**
	 * REVIEW_CHANGE for Ted, Dan.
	 * This is appropriate whilst the DefaultEditorContentPane uses its own
	 * reflection rather than querying the domain model.  Should I switch
	 * the mechansim to use the latter?
	 * @param parent
	 * @param getMethod
	 * @param setMethod
	 * @param configuration
	 * @return
	 */
	public IFormPart createFormPart( 
			Composite parent, 
			Method getMethod, 
			Method setMethod, 
			Object configuration );
}
