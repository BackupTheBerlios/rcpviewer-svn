/**
 * 
 */
package de.berlios.rcpviewer.gui.editors.parts;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

/**
 * Container for one alternate presentation of a collection reference's data.
 * @author Mike
 */
class CollectionGuiPage {
	
	private final Composite _parent;
	private final String _description;
	
	private Viewer _viewer = null;
	
	CollectionGuiPage( Composite parent, String description ) {
		assert parent != null;
		assert description != null;
		_parent = parent;
		_description = description;

	}
	
	Composite getComposite() {
		return _parent;
	}
	
	String getDescription(){
		return _description;
	}
	
	void setViewer( Viewer viewer ) {
		assert viewer != null;
		_viewer = viewer;
	}
	
	Viewer getViewer() {
		return _viewer;
	}

	
}