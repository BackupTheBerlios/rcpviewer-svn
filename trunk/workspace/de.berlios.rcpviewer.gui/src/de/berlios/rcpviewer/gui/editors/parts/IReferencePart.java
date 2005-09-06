package de.berlios.rcpviewer.gui.editors.parts;

import org.eclipse.ui.forms.widgets.ExpandableComposite;

interface IReferencePart extends IGuiPart {
	
	static final int EXPANDABLE_STYLE =
		ExpandableComposite.TREE_NODE 
		| ExpandableComposite.LEFT_TEXT_CLIENT_ALIGNMENT 
		| ExpandableComposite.CLIENT_INDENT;

}
