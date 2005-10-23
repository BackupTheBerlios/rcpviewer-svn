/**
 * 
 */
package de.berlios.rcpviewer.gui.editors.parts;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * DnD for reference collection.
 * @author Mike
 */
class CollectionDragSourceListener implements DragSourceListener {
	
	private final Viewer _viewer;
	
	CollectionDragSourceListener( Viewer viewer ) {
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