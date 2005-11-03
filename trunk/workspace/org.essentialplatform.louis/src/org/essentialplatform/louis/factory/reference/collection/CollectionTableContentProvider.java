package org.essentialplatform.louis.factory.reference.collection;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import org.essentialplatform.runtime.session.DomainObjectAttributeEvent;
import org.essentialplatform.runtime.session.ExtendedDomainObjectAttributeEvent;
import org.essentialplatform.runtime.session.IDomainObject;
import org.essentialplatform.runtime.session.IDomainObjectAttributeListener;
import org.essentialplatform.core.domain.IDomainClass;

/**
 * Extracts pojo collection contents and wraps in IDomainObjects 
 * - does explicitly - does not rely on aspect introduction.
 * <br>This is also responsible for tieing listeners to the domain object 
 * that update the display whenever domain object's attributes are updated.
 */
class CollectionTableContentProvider implements IStructuredContentProvider {
	
	private static final IDomainObject[] EMPTY_ARRY = new IDomainObject[0];
	
	private final IDomainObjectAttributeListener _attListener;
	
	private TableViewer _viewer = null;
	
	/**
	 * Constructor requires reference
	 * @param parent
	 * @param ref
	 */
	CollectionTableContentProvider() {
		_attListener = new IDomainObjectAttributeListener(){
			public void attributeChanged(DomainObjectAttributeEvent event) {
				if ( _viewer != null ) {
					_viewer.update( event.getSource(), null );
				}
			}
			public void attributePrerequisitesChanged(ExtendedDomainObjectAttributeEvent event) {
				// does nowt
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object obj) {
		if ( obj == null ) return new Object[0];
		assert obj instanceof List;
		return ((List)obj).toArray();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// does nowt
	}

	/**
	 *  Note this event handle attaching and disposing of listeners
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
		// record viewer reference if necessary
		if ( _viewer == null && viewer != null && viewer instanceof TableViewer ) {
			_viewer = (TableViewer)viewer;
		}
		
		// remove listeners from old object
		if ( oldInput != null && oldInput instanceof List ) {
			removeListeners( (List<IDomainObject<?>>)oldInput) ;
		}
		
		// add listeners to new object
		if ( newInput != null && newInput instanceof List ) {
			addListeners( (List<IDomainObject<?>>)newInput );
		}
	}
	
	/* private methods */
	
	// add listener to every attribute of every element
	private void addListeners( List<IDomainObject<?>> elements ) {
		assert elements != null;
		for ( IDomainObject<?> element : elements ) {
			for ( EAttribute att : element.getDomainClass().eAttributes() ) {
				element.getAttribute( att ).addListener( _attListener );
			}
		}
	}
	
	// remove listener from every attribute of every element
	private void removeListeners( List<IDomainObject<?>> elements ) {
		assert elements != null;
		for ( IDomainObject<?> element : elements ) {
			for ( EAttribute att : element.getDomainClass().eAttributes() ) {
				element.getAttribute( att ).removeListener( _attListener );
			}
		}
	}
}