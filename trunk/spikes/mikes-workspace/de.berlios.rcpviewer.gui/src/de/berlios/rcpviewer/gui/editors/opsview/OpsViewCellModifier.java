/**
 * 
 */
package de.berlios.rcpviewer.gui.editors.opsview;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author Mike
 */
class OpsViewCellModifier implements ICellModifier {
	
	static final String LABEL_COLUMN = new String("LabelColumn"); //$NON-NLS-1$
	static final String VALUE_COLUMN = new String("ValueColumn"); //$NON-NLS-1$
	
	private final TreeViewer _parent;
	
	/**
	 * 
	 */
	OpsViewCellModifier( TreeViewer parent ) {
		assert parent != null;
		_parent = parent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
		if ( element == null ) throw new IllegalArgumentException();
		if ( !(element instanceof OpsViewParameterProxy) ) return false;
		return VALUE_COLUMN.equals( property );
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property) {
		if ( element == null ) throw new IllegalArgumentException();
		if ( !(element instanceof OpsViewParameterProxy) ) {
			throw new IllegalArgumentException();
		}
		return ((OpsViewParameterProxy)element).getValue();
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
		if ( !(data instanceof OpsViewParameterProxy) ) {
			throw new IllegalArgumentException();
		}
		OpsViewParameterProxy proxy = (OpsViewParameterProxy)data;
		proxy.setValue( value );
		// update parent too for colours
		_parent.update( new Object[]{ proxy, proxy.getParent() }, 
						new String[]{ property } );
	}

}
