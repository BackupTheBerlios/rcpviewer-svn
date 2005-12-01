package org.essentialplatform.louis.factory.reference.collection;

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.event.DomainObjectAttributeEvent;
import org.essentialplatform.runtime.domain.event.ExtendedDomainObjectAttributeEvent;
import org.essentialplatform.runtime.domain.event.IDomainObjectAttributeListener;

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
		assert obj instanceof Collection;
		Collection elementsAsList = (Collection)obj;
		Object[] elementsAsArray = new Object[elementsAsList.size()];
		int i = 0;
		for(Object element: elementsAsList) {
			elementsAsArray[i++] = element;
		}
		return elementsAsArray;
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
			for ( IDomainClass.IAttribute iAttribute : element.getDomainClass().iAttributes() ) {
				element.getAttribute( iAttribute ).addListener( _attListener );
			}
		}
	}
	
	// remove listener from every attribute of every element
	private void removeListeners( List<IDomainObject<?>> elements ) {
		assert elements != null;
		for ( IDomainObject<?> element : elements ) {
			for ( IDomainClass.IAttribute iAttribute : element.getDomainClass().iAttributes() ) {
				element.getAttribute( iAttribute ).removeListener( _attListener );
			}
		}
	}
}