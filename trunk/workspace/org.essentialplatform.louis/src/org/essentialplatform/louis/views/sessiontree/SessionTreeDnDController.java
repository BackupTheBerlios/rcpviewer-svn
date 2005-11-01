package org.essentialplatform.louis.views.sessiontree;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.essentialplatform.louis.LouisPlugin;

import org.essentialplatform.session.IDomainObject;

/**
 * Wraps a <code>DragSource</code> dynamically modifying this so that DnD
 * operations are specific to classes of domain object. 
 * @author Mike
 */
class SessionTreeDnDController {
	
	private static final Transfer[] EMPTY_TRANSFER_ARRAY = new Transfer[0];
	
	SessionTreeDnDController( final TreeViewer viewer ) {
		assert viewer != null;
		
		final DragSource dragSource = new DragSource( 
				viewer.getTree(), 
				DND.DROP_MOVE | DND.DROP_COPY );
		dragSource.setTransfer( EMPTY_TRANSFER_ARRAY );
		
		dragSource.addDragListener (new DragSourceListener () {
			public void dragStart(DragSourceEvent event) {
				event.doit = false;
				ISelection selection = viewer.getSelection();
				if ( !selection.isEmpty() ) {
					Object obj = ((StructuredSelection)selection).getFirstElement();
					if ( obj instanceof IDomainObject ) {
						event.doit = true;
					}
				}
			}
			public void dragSetData (DragSourceEvent event) {
				Object obj = ((StructuredSelection)viewer.getSelection())
														  .getFirstElement();
				assert obj instanceof IDomainObject;
				event.data = obj;
			}
			public void dragFinished(DragSourceEvent event) {
				// does nowt
			}
		});
		
		viewer.addSelectionChangedListener( new ISelectionChangedListener(){
		    public void selectionChanged(SelectionChangedEvent event) {
		    	Transfer transfer = null;
		    	if ( !event.getSelection().isEmpty() ) {
					Object obj
						= ((StructuredSelection)event.getSelection()).getFirstElement();
					if ( obj instanceof IDomainObject ) {
						transfer = LouisPlugin.getTransfer( 
								((IDomainObject<?>)obj).getPojo().getClass() );
					}
		    	}
		    	if ( transfer == null ) {
		    		dragSource.setTransfer( EMPTY_TRANSFER_ARRAY );
		    	}
		    	else {
		    		dragSource.setTransfer( new Transfer[]{ transfer } ) ;
		    	}
		    }
		});
	}

}
