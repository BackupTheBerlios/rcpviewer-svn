package de.berlios.rcpviewer.gui.editors;

import org.eclipse.swt.widgets.Composite;

/**
 * Type for building editor gui structures.
 * @author Mike
 */
public interface IEditorContentBuilder {
	
	public static final String EXTENSION_POINT
		= "de.berlios.rcpviewer.gui.editorcontentbuilder"; 
	public static final String CLASS_PROPERTY = "class";

	/**
	 * Whether can be used to create a GUI for the passed class.
	 * @param clazz
	 * @return
	 */
	public boolean isApplicable( Class clazz );

	/**
	 * User friendly display value when this has to be selected form a list of
	 * options
	 * @return
	 */
	public String getDisplay();
	
	/**
	 * Generates a GUI on the passed parent for the passed object.
	 * @param parent
	 * @param instance
	 */
	public void createGui( Composite parent, Object instance );
}
