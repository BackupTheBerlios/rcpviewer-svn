/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

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
class ActionsViewLabelProvider extends LabelProvider 
		implements ITableLabelProvider, IColorProvider {
	
	private final Color _disabledColour;

	/**
	 * 
	 */
	ActionsViewLabelProvider() {
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
				else if ( element instanceof ActionsViewActionProxy ) {
					return ((ActionsViewActionProxy)element).getText();
				}
				else if ( element instanceof ActionsViewParameterProxy ) {
					return ((ActionsViewParameterProxy)element).getDisplayLabel();
				}
				else {
					throw new IllegalArgumentException();
				}
			case 1: // value column
				 if ( element instanceof ActionsViewParameterProxy ) {
					 return ((ActionsViewParameterProxy)element).getDisplayValue();
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
		if ( element instanceof ActionsViewParameterProxy ) {
			if ( !((ActionsViewParameterProxy)element).isValid() ) {
				return _disabledColour;
			}
		}
		else if ( element instanceof ActionsViewActionProxy ) {
			if ( !((ActionsViewActionProxy)element).isValid() ) {
				return _disabledColour;
			}
		}
		return null;
	}
}
