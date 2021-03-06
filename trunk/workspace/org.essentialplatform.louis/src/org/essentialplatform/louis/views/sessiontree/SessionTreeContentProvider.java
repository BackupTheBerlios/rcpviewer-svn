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

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.filters.InstantiableClassFilter;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.RuntimePlugin;

/**
 * @author Mike
 */
class SessionTreeContentProvider implements ITreeContentProvider {
	
	private IClientSession _session;
	
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
		if ( !(element instanceof IDomainClass)) {
			throw new IllegalArgumentException("Not an instance of IDomainClass");
		}
		assert _session != null;
		return _session.footprintFor( (IDomainClass)element ).toArray(); 
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
		else if ( inputElement instanceof IClientSession ) {
			_session = (IClientSession)inputElement;
			List<IDomainClass> populatedClasses = new ArrayList<IDomainClass>();

			for(IDomainClass domainClass: DomainRegistryUtil.allClasses(new InstantiableClassFilter())) {
				if ( !_session.footprintFor( domainClass ).isEmpty() ) { 
					populatedClasses.add( domainClass );
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
