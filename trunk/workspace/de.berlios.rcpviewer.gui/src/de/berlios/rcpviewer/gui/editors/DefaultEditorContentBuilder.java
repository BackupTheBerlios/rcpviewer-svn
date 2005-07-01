package de.berlios.rcpviewer.gui.editors;

import org.eclipse.ui.forms.IManagedForm;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Creates a scrolled form with a column of labels and a column of 
 * fields as presented by the <code>FieldBuilderFactory</code>.
 */
public class DefaultEditorContentBuilder implements IEditorContentBuilder {



	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IEditorContentBuilder#isApplicable(java.lang.Class)
	 */
	public boolean isApplicable( Class clazz ) {
		// default - always applicable
		return true;
	}
	

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IEditorContentBuilder#getDisplay()
	 */
	public String getDisplay() {
		return GuiPlugin.getResourceString( "DefaultEditorContentBuilder.Display");
	}



	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IEditorContentBuilder#createGui(org.eclipse.ui.forms.IManagedForm, de.berlios.rcpviewer.session.IDomainObject)
	 */
	public void createGui(IManagedForm parent, IDomainObject instance ) {		
		if ( parent == null ) throw new IllegalArgumentException();
		if ( instance == null ) throw new IllegalArgumentException();
		
		parent.addPart( new DefaultEditorContentPart(parent, instance ) );
	}

}
