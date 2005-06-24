package de.berlios.rcpviewer.gui.editors;

import org.eclipse.swt.widgets.Composite;

/**
 * Type for building individual fields within a gui.
 * @author Mike
 */
public interface IFieldBuilder {
	
	public static final String EXTENSION_POINT
		= "de.berlios.rcpviewer.gui.fieldbuilder"; 
	public static final String CLASS_PROPERTY = "class";
	
	public boolean isApplicable( Class clazz, Object value );

	public void createGui( Composite parent, Object value );
}
