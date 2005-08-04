/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import org.eclipse.emf.ecore.EParameter;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.dnd.Transfer;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.celleditors.CellEditorFactory;
import de.berlios.rcpviewer.gui.dnd.DndTransferFactory;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Wraps a parameter for an op
 * @author Mike
 */
class OpsViewParameterProxy {

	private final OpsViewActionProxy _parent;
	private final EParameter _param;
	
	// lazily instantiated
	private String _label = null;
	private Transfer _transfer = null;
	private CellEditor _cellEditor = null;
	
	// set dynamically
	private Object _value = null;
	
	/**
	 * Constructor requires parent and parameter
	 * @param parent
	 * @param param
	 */
	OpsViewParameterProxy( 
			OpsViewActionProxy parent, EParameter param) {
		assert parent != null;
		assert param != null;
		_parent = parent;
		_param = param;
	}
	
	/**
	 * Display text for label
	 * @return
	 */
	String getDisplayLabel() {
		if ( _label == null ) {
			StringBuffer sb = new StringBuffer();
			sb.append( _param.getName() );
			sb.append( " ["); //$NON-NLS-1$
			sb.append( _param.getEType().getInstanceClass().getSimpleName() );
			sb.append( "]"); //$NON-NLS-1$
			_label = sb.toString();
		}
		return _label;
	}
	
	/**
	 * Display text for value
	 * @return
	 */
	String getDisplayValue() {
		if ( _value == null ) return ""; //$NON-NLS-1$
		if ( _value instanceof IDomainObject ) {
			return GuiPlugin.getDefault().getLabelProvider( 
					(IDomainObject)_value ).getText( _value );
		}
		else {
			return String.valueOf( _value );
		}
	}
	
	/**
	 * Return the appropriate cell editor for the parameter.
	 * @return
	 */
	CellEditor getCellEditor() {
		if ( _cellEditor == null ) {
			_cellEditor = CellEditorFactory.getCellEditor(
							_param.getEType().getInstanceClass() );
		}
		return _cellEditor;
	}

	/**
	 * @return Returns the parent.
	 */
	OpsViewActionProxy getParent() {
		return _parent;
	}
	
	/**
	 * Sets the value.
	 * @param value
	 */
	void setValue( Object value ) {
		_value = value;
	}
	
	/**
	 * @return
	 */
	Object getValue() {
		return _value;
	}
	
	/**
	 * Accessor and lazy instantiator
	 * @return
	 */
	Transfer getTransfer() {
		if ( _transfer == null ) {
			_transfer = DndTransferFactory.getTransfer( 
					_param.getEType().getInstanceClass() );
			assert _transfer != null;
		}
		return _transfer;
	}
	
	/**
	 * Whether the value is for valid to be used in the operation.
	 * <br>Currenly simply a <code>null</code> check.
	 * @return
	 */
	boolean isValid() {
		return getValue() != null;
	}
	

}
