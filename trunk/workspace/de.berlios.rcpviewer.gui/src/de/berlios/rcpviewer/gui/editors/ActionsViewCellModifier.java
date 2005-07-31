/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author Mike
 */
class ActionsViewCellModifier implements ICellModifier {
	
	static final String LABEL_COLUMN = new String("LabelColumn"); //$NON-NLS-1$
	static final String VALUE_COLUMN = new String("ValueColumn"); //$NON-NLS-1$
	
	private final TreeViewer _parent;
	
	/**
	 * 
	 */
	ActionsViewCellModifier( TreeViewer parent ) {
		assert parent != null;
		_parent = parent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
		if ( element == null ) throw new IllegalArgumentException();
		if ( !(element instanceof ActionsViewParameterProxy) ) return false;
		return VALUE_COLUMN.equals( property );
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property) {
		if ( element == null ) throw new IllegalArgumentException();
		if ( !(element instanceof ActionsViewParameterProxy) ) {
			throw new IllegalArgumentException();
		}
		return ((ActionsViewParameterProxy)element).getValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) {
		if ( element == null ) throw new IllegalArgumentException();
		if ( !(element instanceof TreeItem) ) {
			throw new IllegalArgumentException();
		}
		Object data = ((TreeItem)element).getData();
		if ( !(data instanceof ActionsViewParameterProxy) ) {
			throw new IllegalArgumentException();
		}
		ActionsViewParameterProxy proxy = (ActionsViewParameterProxy)data;
		proxy.setValue( value );
		// update parent too for colours
		_parent.update( new Object[]{ proxy, proxy.getParent() }, 
						new String[]{ property } );
	}

}
