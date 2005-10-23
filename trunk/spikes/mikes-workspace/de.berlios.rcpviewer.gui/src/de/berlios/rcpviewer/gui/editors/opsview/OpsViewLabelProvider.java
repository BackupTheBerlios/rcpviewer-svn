/**
 * 
 */
package de.berlios.rcpviewer.gui.editors.opsview;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import de.berlios.rcpviewer.gui.widgets.ErrorInput;

/**
 * @author Mike
 *
 */
class OpsViewLabelProvider extends LabelProvider 
		implements ITableLabelProvider, IColorProvider {
	
	private final Color _disabledColour;

	/**
	 * 
	 */
	OpsViewLabelProvider() {
		_disabledColour = Display.getCurrent().getSystemColor( SWT.COLOR_DARK_GRAY );
	}
	
	/* ITableLabelProvider contract (partial) */

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		if ( element == null ) throw new IllegalArgumentException();
		switch( columnIndex ) {
			case 0: // label column
				if ( element instanceof ErrorInput ) {
					return ((ErrorInput)element).getMessage();
				}
				else if ( element instanceof OpsViewActionProxy ) {
					return ((OpsViewActionProxy)element).getText();
				}
				else if ( element instanceof OpsViewParameterProxy ) {
					return ((OpsViewParameterProxy)element).getDisplayLabel();
				}
				else {
					throw new IllegalArgumentException();
				}
			case 1: // value column
				 if ( element instanceof OpsViewParameterProxy ) {
					 return ((OpsViewParameterProxy)element).getDisplayValue();
				 }
				 else {
					 return ""; //$NON-NLS-1$
				 }
			default:
				throw new IllegalArgumentException();
		}
	}
	
	/* IColorProvider contract */
	
	/**
	 * Always returns null;
	 * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
	 */
	public Color getBackground(Object element) {
		return null;
	}

	/**
	 * Default colour if proxy OK else disabled color
	 * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
	 */
	public Color getForeground(Object element) {
		if ( element == null ) throw new IllegalArgumentException();
		if ( element instanceof OpsViewParameterProxy ) {
			if ( !((OpsViewParameterProxy)element).isValid() ) {
				return _disabledColour;
			}
		}
		else if ( element instanceof OpsViewActionProxy ) {
			if ( !((OpsViewActionProxy)element).isValid() ) {
				return _disabledColour;
			}
		}
		return null;
	}
}
