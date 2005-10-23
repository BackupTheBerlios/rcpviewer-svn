package de.berlios.rcpviewer.gui.editors.parts;

import java.util.ArrayList;
import java.util.List;

import net.sf.plugins.utils.SWTUtils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.progress.UIJob;

import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.widgets.ErrorInput;
import de.berlios.rcpviewer.session.DomainObjectAttributeEvent;
import de.berlios.rcpviewer.session.DomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectAttributeListener;
import de.berlios.rcpviewer.session.IDomainObjectReferenceListener;
import de.berlios.rcpviewer.session.ISession;

/**
 * Extracts pojo collection contents and wraps in IDomainObjects 
 * - does explicitly - does not rely on aspect introduction
 * Notes:
 * <ul>
 * <li>this is also used to tie listeners to the object that updates 
 * the display whenever domain object or its attributes updated;
 * <li>this is used by both List and Table viewers
 * </ul>
 */
class CollectionContentProvider implements IStructuredContentProvider {
	
	private static final IDomainObject[] EMPTY_ARRY = new IDomainObject[0];
	
	private final EReference _ref;
	private final IDomainObjectReferenceListener _refListener;
	private final IDomainObjectAttributeListener _attListener;
	
	private StructuredViewer _viewer = null;

	/**
	 * Constructor requires parent form and reference
	 * @param parent
	 * @param ref
	 */
	CollectionContentProvider( final IFormPart parent, EReference ref ) {
		assert parent != null;
		assert ref != null;
		_ref = ref;
		_refListener = new IDomainObjectReferenceListener(){
			public void collectionAddedTo(DomainObjectReferenceEvent event) {
				parent.refresh();
				redrawViewer();
			}
			public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
				parent.refresh();
				redrawViewer();
			}
			public void referenceChanged(DomainObjectReferenceEvent event) {
				// does nowt
			}
		};
		_attListener = new IDomainObjectAttributeListener(){
			public void attributeChanged(DomainObjectAttributeEvent event) {
				if ( _viewer != null ) {
					_viewer.update( event.getSource(), null );
				}
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object obj) {
		if ( obj == null ) return new Object[0];
		assert obj instanceof IDomainObject;
		try {
			return getCollectionDomainObjects( (IDomainObject)obj );
		}
		catch ( CoreException ce ) {
			GuiPlugin.getDefault().getLog().log( ce.getStatus() );
			return new Object[] { new ErrorInput() } ;
		}
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
		
		// record viewer
		if ( _viewer == null && viewer != null ){
			assert viewer instanceof StructuredViewer;
			_viewer = (StructuredViewer)viewer;
		}
		
		// remove listeners from old object
		if ( oldInput != null && oldInput instanceof IDomainObject ) {
			removeListeners( (IDomainObject<?>)oldInput) ;
		}
		
		// add listeners to new object
		if ( newInput != null && newInput instanceof IDomainObject ) {
			addListeners( (IDomainObject<?>)newInput );
		}
		
		// repack if a table viewer - however must do async as this method has
		// been called BEFORE the new input is set on the viewer
		if ( newInput != null && _viewer instanceof TableViewer ) {
			UIJob job = new UIJob( "" ){ //$NON-NLS-1$
				public IStatus runInUIThread(IProgressMonitor monitor) {
					redrawViewer();
					return Status.OK_STATUS;
				}
			};
			job.schedule();
		}
	}
	
	/* private methods */
	
	// returns the domain objects for all the collection's pojo's
	private IDomainObject[] getCollectionDomainObjects(
			IDomainObject<?> dObj ) throws CoreException {
		assert dObj != null;
		ISession session = RuntimePlugin.getDefault().getSessionManager().get(
					dObj.getSessionId() );
		List<IDomainObject> elements = new ArrayList<IDomainObject>();
		Class<?> collectionPojoType = _ref.getEType().getInstanceClass();
		for( Object pojo : dObj.getCollectionReference( _ref ).getCollection() ) {
			IDomainObject dElem = session.getDomainObjectFor( 
					pojo, collectionPojoType );
			assert dElem != null;
			elements.add( dElem );	
		}
		return elements.toArray( EMPTY_ARRY );	
	}
	
	
	// as it says
	private void addListeners( IDomainObject<?> dObj ) {
		assert dObj != null;
		
		// reference listener
		dObj.getCollectionReference( _ref ).addListener( _refListener );
		
		// attribute listener - one for each attribute on each collection element 
		try {
			IDomainObject<?>[] children = getCollectionDomainObjects( dObj );
			if ( children.length == 0 ) return;
			List<EAttribute> attributes = children[0].getDomainClass().attributes();
			for ( IDomainObject<?> child : children ) {
				for ( EAttribute att : attributes ) {
					child.getAttribute( att ).addListener( _attListener );
				}
			}
		}
		catch ( CoreException ce ) {
			GuiPlugin.getDefault().getLog().log( ce.getStatus() );
		}
	}
	
	// as it says
	private void removeListeners( IDomainObject<?> dObj ) {
		// reference listener
		dObj.getCollectionReference( _ref ).removeListener( _refListener );
		
		// attribute listener - one for each attribute on each collection element 
		try {
			IDomainObject<?>[] children = getCollectionDomainObjects( dObj );
			if ( children.length == 0 ) return;
			List<EAttribute> attributes = children[0].getDomainClass().attributes();
			for ( IDomainObject<?> child : children ) {
				for ( EAttribute att : attributes ) {
					child.getAttribute( att ).removeListener( _attListener );
				}
			}
		}
		catch ( CoreException ce ) {
			GuiPlugin.getDefault().getLog().log( ce.getStatus() );
		}
	}
	
	// if a table, causes it to be redrawn
	private void redrawViewer() {
		if ( _viewer != null && _viewer instanceof TableViewer ) {
			Table table = ((TableViewer)_viewer).getTable();
			try {
				table.setRedraw( false );
//				SWTUtils.packTableColumns( table );
//				SWTUtils.autosizeColumn( table, 0 );
				SWTUtils.evenlyWidenColumns( table );
			}
			finally {
				table.setRedraw( true );
			}
		}	
	}
}