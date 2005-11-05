/**
 * 
 */
package org.essentialplatform.louis.factory.reference.collection;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

import org.essentialplatform.runtime.domain.IDomainObject;

/**
 * DnD for reference collection.
 * @author Mike
 */
class CollectionTableDragSourceListener implements DragSourceListener {
	
	private final Viewer _viewer;
	
	CollectionTableDragSourceListener( Viewer viewer ) {
		assert viewer != null;
		_viewer = viewer;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragStart(DragSourceEvent event) {
		event.doit = false;
		ISelection selection = _viewer.getSelection();
		if ( !selection.isEmpty() ) {
			Object obj = ((StructuredSelection)selection).getFirstElement();
			if ( obj instanceof IDomainObject ) {
				event.doit = true;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragSetData (DragSourceEvent event) {
		Object obj = ((StructuredSelection)_viewer.getSelection())
												  .getFirstElement();
		assert obj instanceof IDomainObject;
		event.data = obj;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragFinished(DragSourceEvent event) {
		// does nowt
	}
}