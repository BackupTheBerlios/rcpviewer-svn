package de.berlios.rcpviewer.gui.editors.parts;

import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

interface IReferencePart extends IFormPart {
	
	static final int EXPANDABLE_STYLE =
		ExpandableComposite.TREE_NODE 
		| ExpandableComposite.LEFT_TEXT_CLIENT_ALIGNMENT 
		| ExpandableComposite.CLIENT_INDENT;

}
