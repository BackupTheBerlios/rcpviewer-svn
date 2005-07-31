package de.berlios.rcpviewer.gui.editors;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.TreeItem;

import de.berlios.rcpviewer.gui.dnd.DndTransferFactory;

/**
 * Wraps a <code>DropTarget</code> dynamically modifying this so that DnD
 * operations are specific to the selected parameter 
 * @author Mike
 */
class ActionsViewDnDController {
	
	private static final Transfer[] EMPTY_TRANSFER_ARRAY = new Transfer[0];
	
	ActionsViewDnDController( final TreeViewer viewer ) {
		assert viewer != null;
		
		final DropTarget target = new DropTarget( 
				viewer.getTree(), 
				DND.DROP_MOVE | DND.DROP_COPY );
		target.setTransfer( DndTransferFactory.getTransfers() ) ;
		target.addDropListener ( new DropTargetAdapter() {
			public void dragEnter(DropTargetEvent event){
				if ( event.detail == DND.DROP_NONE ) {
					event.detail = DND.DROP_MOVE;
				}
			}
			public void dragOver(DropTargetEvent event){
				if ( event.item != null && event.item instanceof TreeItem ) {
					TreeItem item = (TreeItem)event.item;
					if ( item.getData() instanceof ActionsViewParameterProxy ) {
						Transfer transfer =
							((ActionsViewParameterProxy)item.getData()).getTransfer();
						for ( TransferData data : event.dataTypes ) {
							if ( transfer.isSupportedType( data ) ) {
								event.detail = DND.DROP_MOVE;
								return;
							}
						}
					}
					if ( item.getData() instanceof ActionsViewActionProxy ) {
						if ( !item.getExpanded() ) {
							// only expand if one or more params fits the 
							// dragged item
							boolean expand = false;
							paramLoop: for ( ActionsViewParameterProxy param :
									((ActionsViewActionProxy)item.getData()).getParams() ) {
								Transfer transfer = param.getTransfer();
								for ( TransferData data : event.dataTypes ) {
									if ( transfer.isSupportedType( data ) ) {
										expand = true;
										break paramLoop;
									}
								}
							}
							if ( expand ) {
								viewer.expandToLevel( 
										item.getData(), 
										AbstractTreeViewer.ALL_LEVELS );
							}
						}
						
					}
				}
				event.detail = DND.DROP_NONE;
			}
			public void drop(DropTargetEvent event) {
				TreeItem item = (TreeItem)event.item;
				if ( event.data != null &&
						item.getData() instanceof ActionsViewParameterProxy ) {
					ActionsViewParameterProxy param =
						((ActionsViewParameterProxy)item.getData());
					param.setValue( event.data );
					viewer.update( new Object[]{ param, param.getParent() },
								   null );
					return;
				}
				event.detail = DND.DROP_NONE;
			}
		});
	}
	
	// commonly used
	private ActionsViewParameterProxy getSelectedParam( TreeViewer viewer) {
		assert viewer != null;
    	if ( viewer.getSelection().isEmpty() ) return null;
    	Object obj = ((StructuredSelection)
    			viewer.getSelection()).getFirstElement();
    	if ( obj instanceof ActionsViewParameterProxy ) {
    		return (ActionsViewParameterProxy)obj;
    	}
    	return null;
	}
	
	private TreeItem getItem( TreeViewer viewer, DropTargetEvent event ) {
		assert viewer != null;
		assert event != null;
		return null;
	}

}
