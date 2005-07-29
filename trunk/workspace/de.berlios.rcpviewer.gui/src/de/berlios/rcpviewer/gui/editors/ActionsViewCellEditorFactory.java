package de.berlios.rcpviewer.gui.editors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Tree;

/**
 * @author Mike
 */
class ActionsViewCellEditorFactory {
	
	private final CellEditor[] _array;
	
	/**
	 */
	ActionsViewCellEditorFactory() {
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
		else if ( selection instanceof ActionsViewParameterProxy ) {
    		_array[1] = ((ActionsViewParameterProxy)selection).getCellEditor();
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
