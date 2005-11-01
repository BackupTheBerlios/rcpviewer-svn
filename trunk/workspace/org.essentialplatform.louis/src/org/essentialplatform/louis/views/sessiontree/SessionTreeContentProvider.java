/**
 * 
 */
package org.essentialplatform.louis.views.sessiontree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.util.DomainRegistryUtil;
import org.essentialplatform.louis.widgets.ErrorInput;

import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.runtime.RuntimePlugin;
import org.essentialplatform.session.IDomainObject;
import org.essentialplatform.session.ISession;

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
				return session.footprintFor( (IDomainClass)element ).toArray(); 
			}
			catch ( CoreException ce ) {
				LouisPlugin.getDefault().getLog().log( ce.getStatus() );
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
			return ((IDomainObject<?>)element).getDomainClass();
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

			Iterator<IDomainClass> it = DomainRegistryUtil.iterateAllClasses(
					DomainRegistryUtil.Filter.INSTANTIABLE );
			while ( it.hasNext() ) {
				IDomainClass clazz = it.next();
				if ( !session.footprintFor( clazz ).isEmpty() ) { 
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
