/**
 * 
 */
package de.berlios.rcpviewer.gui.editors.parts;

import java.util.Collection;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.dnd.DomainObjectTransfer;
import de.berlios.rcpviewer.gui.jobs.AddReferenceJob;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * DnD for reference collection.
 * @author Mike
 */
class CollectionDropTargetAdapter extends DropTargetAdapter {
	
	private final Viewer _dtViewer;
	private final EReference _ref;
	private final DomainObjectTransfer _transfer;
	private boolean _checkUnique = false;
	
	CollectionDropTargetAdapter( 
			Viewer viewer,
			EReference ref, 
			DomainObjectTransfer 
			transfer ) {
		super();
		assert viewer != null;
		assert ref != null;
		assert transfer != null;
		_dtViewer = viewer;
		_ref = ref;
		_transfer = transfer;
		_checkUnique = _ref.isUnique();
	}
	

	/**
	 * Ensure DnD will work
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragEnter(DropTargetEvent event) {
		if ( event.detail == DND.DROP_NONE ) {
			event.detail = DND.DROP_MOVE;
		}
	}
	
	/**
	 * If possible, visually indicate unique constraints
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOver(DropTargetEvent event){		
		if ( _checkUnique ) {
			// this only works on some platforms...
			IDomainObject<?> dObj = (IDomainObject<?>)
				_transfer.nativeToJava(event.currentDataType);
			// ...so if returns null don't bother any more
			if ( dObj == null ) {
				_checkUnique = false;
			}
			else {
				if ( getCollection().contains( dObj.getPojo() ) ) {
					event.detail = DND.DROP_NONE;
				}
				else {
					event.detail = DND.DROP_MOVE;
				}
			}
		}
	}	
	
	
	/**
	 * Adds reference
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop(DropTargetEvent event) {
		if ( event.data != null 
				&& event.data instanceof IDomainObject ) {
			IDomainObject<?> dObj = (IDomainObject<?>)event.data;
			// if cannot check uniqueness in dragOver, do now
			if ( _ref.isUnique() && !_checkUnique ) {
				if( getCollection().contains( dObj.getPojo() ) ) {
					MessageDialog.openError(
							null,
							GuiPlugin.getResourceString( 
									"CollectionPart.UniqueTitle" ), //$NON-NLS-1$
							GuiPlugin.getResourceString( 
									"CollectionPart.UniqueMsg" ) ); //$NON-NLS-1$
					return;
				}
			}
			// ok - add
			Job job = new AddReferenceJob(
					(IDomainObject<?>)_dtViewer.getInput(),
					_ref,
					dObj );
			job.schedule();
		}
		else {
			event.detail = DND.DROP_NONE;
		}
	}
	
	private Collection<?> getCollection(){
		IDomainObject<?> parent= (IDomainObject<?>)_dtViewer.getInput();
		return parent.getCollectionReference( _ref ).getCollection();
	}
}