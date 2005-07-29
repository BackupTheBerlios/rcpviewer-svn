/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.berlios.rcpviewer.gui.widgets.ErrorInput;

/**
 * @author Mike
 *
 */
class ActionsViewLabelProvider extends LabelProvider 
		implements ITableLabelProvider {

	/**
	 * 
	 */
	ActionsViewLabelProvider() {
		super();
	}

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
}
