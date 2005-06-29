package de.berlios.rcpviewer.gui.editors;

import org.eclipse.ui.forms.IManagedForm;

/**
 * Type for building editor gui structures.
 * @author Mike
 */
public interface IEditorContentBuilder {

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
	
	
	// REVIEW_CHANGE for mike: create UI for a managed form instead of composite -- ted
	/**
	 * Generates a GUI on the passed parent for the passed object.
	 * @param parent
	 * @param instance
	 */
	public void createGui( IManagedForm parent, Object instance );
}
