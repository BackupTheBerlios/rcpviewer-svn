/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import java.util.List;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.widgets.ErrorInput;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * @author Mike
 */
class ActionsViewContentProvider implements ITreeContentProvider {

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
		IDomainObject<?> obj = (IDomainObject)inputElement;
		List<EOperation> ops = obj.getDomainClass().operations(); 
		if ( ops.isEmpty() ) {
			return new Object[]{ new ErrorInput( 
				GuiPlugin.getResourceString( "ActionsViewContentProvider.NoOps" )) }; //$NON-NLS-1$
		}
		else {
			int num = ops.size();
			ActionsViewActionProxy[] proxies = new ActionsViewActionProxy[num];
			for ( int i=0 ; i < num ; i++ ) {
				proxies[i] = new ActionsViewActionProxy( obj, ops.get(i) );
			}
			return proxies;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement) {
		if ( parentElement instanceof ActionsViewActionProxy ) {
			return ((ActionsViewActionProxy)parentElement).getParams();
		}
		return new Object[0];
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		if ( element instanceof ActionsViewParameterProxy ) {
			return ((ActionsViewParameterProxy)element).getParent();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		if ( element instanceof ActionsViewActionProxy ) {
			return ((ActionsViewActionProxy)element).getParams().length > 0;
		}
		return false;
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
