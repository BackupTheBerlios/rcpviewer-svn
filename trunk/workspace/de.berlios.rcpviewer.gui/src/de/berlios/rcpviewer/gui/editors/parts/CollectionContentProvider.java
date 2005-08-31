package de.berlios.rcpviewer.gui.editors.parts;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IFormPart;

import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.widgets.ErrorInput;
import de.berlios.rcpviewer.session.DomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectReferenceListener;
import de.berlios.rcpviewer.session.ISession;

/**
 * Extracts pojo collection contents and wraps in IDomainObjects 
 * - does explicitly - does not rely on aspect introduction
 * Note this is also used to tie a listener to the object that updates 
 * the display whenever domain object updated
 */
class CollectionContentProvider implements IStructuredContentProvider {
	
	private final EReference _ref;
	private final IDomainObjectReferenceListener _refListener;

	CollectionContentProvider( final IFormPart parent, EReference ref ) {
		assert parent != null;
		assert ref != null;
		_ref = ref;
		_refListener = new IDomainObjectReferenceListener(){
			public void collectionAddedTo(DomainObjectReferenceEvent event) {
				parent.refresh();
			}
			public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
				parent.refresh();
			}
			public void referenceChanged(DomainObjectReferenceEvent event) {
				// does nowt
			}
		};
	}
	
	public Object[] getElements(Object obj) {
		if ( obj == null ) return new Object[0];
		assert obj instanceof IDomainObject;
		try {
			IDomainObject<?> dObj = (IDomainObject)obj;
			final ISession session
				= RuntimePlugin.getDefault().getSessionManager().get(
					dObj.getSessionId() );
			Collection<IDomainObject> elements
				= new ArrayList<IDomainObject>();
			
			Class<?> collectionPojoType = _ref.getEType().getInstanceClass();
			for( Object pojo : dObj.getCollectionReference( _ref ).getCollection() ) {
				IDomainObject dElem = session.getDomainObjectFor( 
						pojo, collectionPojoType );
				assert dElem != null;
				elements.add( dElem );	
			}
			return elements.toArray();
		}
		catch ( CoreException ce ) {
			GuiPlugin.getDefault().getLog().log( ce.getStatus() );
			return new Object[] { new ErrorInput() } ;
		}
	}
	public void dispose() {
		// does nowt
	}
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// note this handles disposal of the listener too as the viewer's
		// input is set to null
		if ( oldInput != null && oldInput instanceof IDomainObject ) {
			((IDomainObject<?>)oldInput)
				.getCollectionReference( _ref )
				.removeListener( _refListener );
		}
		if ( newInput != null && newInput instanceof IDomainObject ) {
			((IDomainObject<?>)newInput)
				.getCollectionReference( _ref )
				.addListener( _refListener );
		}
		if ( newInput != null && viewer instanceof TableViewer ) {
			for ( TableColumn column : ((TableViewer)viewer).getTable().getColumns() ) {
				column.pack();
			}
		}
	}
}