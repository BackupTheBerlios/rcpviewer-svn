/**
 * 
 */
package de.berlios.rcpviewer.gui.views.sessiontree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.util.DomainRegistryUtil;
import de.berlios.rcpviewer.gui.widgets.ErrorInput;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;

/**
 * @author Mike
 */
class SessionTreeContentProvider implements ITreeContentProvider {
	
	private String _sessionId = null;
	
	/**
	 * No-arg constructor
	 */
	SessionTreeContentProvider( ) {
		super();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object element) {
		if ( element instanceof IDomainClass ) {
			assert _sessionId != null;
			try {
				ISession session = RuntimePlugin.getDefault()
												.getSessionManager()
												.get( _sessionId );
				return session.footprintFor( (IDomainClass)element ).toArray(); // JAVA_5_FIXME
			}
			catch ( CoreException ce ) {
				GuiPlugin.getDefault().getLog().log( ce.getStatus() );
				return new Object[] { new ErrorInput() } ;
			}
		}
		else {
			throw new IllegalArgumentException();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		if ( element instanceof ErrorInput ) {
			return null;
		}
		else if ( element instanceof IDomainClass ) {
			return null;
		}
		else if ( element instanceof IDomainObject ) {
			return ((IDomainObject)element).getDomainClass(); // JAVA_5_FIXME
		}
		else {
			throw new IllegalArgumentException();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		if ( element instanceof ErrorInput ) {
			return false;
		}
		else if ( element instanceof IDomainClass ) {
			return true;
		}
		else if ( element instanceof IDomainObject ) {
			return false;
		}
		else {
			throw new IllegalArgumentException();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		if ( inputElement instanceof ErrorInput ) {
			return new Object[]{ inputElement };
		}
		else if ( inputElement instanceof ISession ) {
			ISession session = (ISession)inputElement;
			_sessionId = session.getId();
			List<IDomainClass> populatedClasses = new ArrayList<IDomainClass>();

			Iterator<IDomainClass> it = DomainRegistryUtil.iterateAllClasses();
			while ( it.hasNext() ) {
				IDomainClass clazz = it.next();
				if ( !session.footprintFor( clazz ).isEmpty() ) { // JAVA_5_FIXME
					populatedClasses.add( clazz );
				}
			}
			return populatedClasses.toArray();
		}
		else {
			throw new IllegalArgumentException();
		}
			
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

}
