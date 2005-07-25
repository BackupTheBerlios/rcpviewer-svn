/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import org.eclipse.jface.viewers.LabelProvider;

import de.berlios.rcpviewer.gui.widgets.ErrorInput;

/**
 * @author Mike
 *
 */
public class ActionsViewLabelProvider extends LabelProvider {

	/**
	 * 
	 */
	ActionsViewLabelProvider() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if ( element == null ) throw new IllegalArgumentException();
		if ( element instanceof ErrorInput ) {
			return ((ErrorInput)element).getMessage();
		}
		else if ( element instanceof ActionsViewActionProxy ) {
			return ((ActionsViewActionProxy)element).getText();
		}
		else if ( element instanceof ActionsViewParameterProxy ) {
			return ((ActionsViewParameterProxy)element).getText();
		}
		else {
			throw new IllegalArgumentException();
		}
	}
}
