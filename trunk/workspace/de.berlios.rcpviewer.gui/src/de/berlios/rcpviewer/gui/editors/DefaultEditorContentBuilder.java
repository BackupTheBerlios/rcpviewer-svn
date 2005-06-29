package de.berlios.rcpviewer.gui.editors;

import org.eclipse.ui.forms.IManagedForm;

/**
 * Creates a scrolled form with a column of labels and a column of 
 * fields as presented by the <code>FieldBuilderFactory</code>.
 */
public class DefaultEditorContentBuilder implements IEditorContentBuilder {

	/* (non-Javadoc)
	 * @see mikespike3.gui.IEditorContentBuilder#isApplicable(java.lang.Class)
	 */
	public boolean isApplicable( Class clazz ) {
		// default - always applicable
		return true;
	}
	
	/* (non-Javadoc)
	 * @see mikespike3.gui.IEditorContentBuilder#getDisplay()
	 */
	public String getDisplay() {
		return "Default Form";
	}

	/**
	 * Currently does model stuff do.
	 */
	public void createGui(IManagedForm parent, Object instance ) {		
		if ( parent == null ) throw new IllegalArgumentException();
		if ( instance == null ) throw new IllegalArgumentException();
		
		DefaultEditorContentPart contentPart= new DefaultEditorContentPart(parent, instance);
		parent.addPart(contentPart);
	}

}
