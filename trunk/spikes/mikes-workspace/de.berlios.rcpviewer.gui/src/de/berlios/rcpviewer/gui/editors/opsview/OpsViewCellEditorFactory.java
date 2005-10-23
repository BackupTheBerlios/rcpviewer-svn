package de.berlios.rcpviewer.gui.editors.opsview;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Tree;

/**
 * @author Mike
 */
class OpsViewCellEditorFactory {
	
	private final CellEditor[] _array;
	
	/**
	 */
	OpsViewCellEditorFactory() {
		_array = new CellEditor[2];
	}
	
	/**
	 * Reports which cell editors the tree should have for a particular
     * selection.
	 * @param tree
	 * @param selection
	 * @return
	 */
	CellEditor[] getCellEditors( Tree tree, Object selection ) {
		if ( selection == null ) {
			_array[1] = null;
		}
		else if ( selection instanceof OpsViewParameterProxy ) {
    		_array[1] = ((OpsViewParameterProxy)selection).getCellEditor();
    		// attach to tree if not already done
    		if ( _array[1].getControl() == null ) {
    			_array[1].create( tree );
    		}
    	}
    	else {
    		_array[1] = null;
    	}
		return _array;
	}
}
