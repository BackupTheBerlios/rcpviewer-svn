/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import org.eclipse.emf.ecore.EParameter;
import org.eclipse.swt.dnd.Transfer;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.dnd.DndUtil;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Wraps a parameter for an action
 * @author Mike
 */
class ActionsViewParameterProxy {

	private final ActionsViewActionProxy _parent;
	private final EParameter _param;
	
	// lazily instantiated
	private String _displayTextPrefix = null;
	private Transfer _transfer = null;
	
	// set dynamically
	private Object _value = null;
	
	/**
	 * Constructor requires parent and parameter
	 * @param parent
	 * @param param
	 */
	ActionsViewParameterProxy( 
			ActionsViewActionProxy parent, EParameter param) {
		assert parent != null;
		assert param != null;
		_parent = parent;
		_param = param;
	}
	
	/**
	 * Display text for label provider
	 * @return
	 */
	String getText() {
		if ( _value == null ) {
			return getDisplayTestPrefix();
		}
		else {
			StringBuffer sb = new StringBuffer();
			sb.append( getDisplayTestPrefix() );
			sb.append( " : " ); //$NON-NLS-1$
			if ( _value instanceof IDomainObject ) {
				sb.append( 
					GuiPlugin.getDefault().getLabelProvider( 
						(IDomainObject)_value ).getText( _value ) );
			}
			else {
				sb.append( _value );
			}
			return sb.toString();
		}
	}

	/**
	 * @return Returns the parent.
	 */
	ActionsViewActionProxy getParent() {
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
			_transfer = DndUtil.getTransfer( 
					_param.getEType().getInstanceClass() );
			assert _transfer != null;
		}
		return _transfer;
	}
	
	
	/* private methods */

	// lazy instantiator
	private String getDisplayTestPrefix(){
		if ( _displayTextPrefix == null ) {
			StringBuffer sb = new StringBuffer();
			sb.append( _param.getName() );
			sb.append( " ["); //$NON-NLS-1$
			sb.append( _param.getEType().getInstanceClass().getSimpleName() );
			sb.append( "]"); //$NON-NLS-1$
			_displayTextPrefix = sb.toString();
		}
		return _displayTextPrefix;
	}

}
