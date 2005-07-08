/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import java.util.List;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.widgets.ErrorInput;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * @author Mike
 */
class ActionsViewContentProvider implements IStructuredContentProvider {

	/**
	 * 
	 */
	ActionsViewContentProvider() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		if ( inputElement == null ) throw new IllegalArgumentException();
		if ( !( inputElement instanceof IDomainObject ) ) {
			throw new IllegalArgumentException();
		}
		List<EOperation> ops = 
			((IDomainObject)inputElement).getDomainClass().operations(); // JAVA_5_FIXME
		if ( ops.isEmpty() ) {
			return new Object[]{ new ErrorInput( 
				GuiPlugin.getResourceString( "ActionsViewContentProvider.NoOps" )) }; //$NON-NLS-1$
		}
		else {
			return ops.toArray();
		}
	}

	/**
	 * Null method
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// does nowt
	}

	/**
	 * Null method
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// does nowt
	}

}
