/**
 * 
 */
package org.essentialplatform.louis.factory.reference.collection;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.dnd.IAccessibleObjectTransfer;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * DnD for reference collection.
 * @author Mike
 */
class CollectionTableDropTargetAdapter extends DropTargetAdapter {
	
	private final CollectionTablePart _part;
	private final EReference _ref;
	private final Transfer _transfer;
	private boolean _checkUnique = false;
	
	CollectionTableDropTargetAdapter( 
			CollectionTablePart part,
			EReference ref, 
			Transfer transfer ) {
		super();
		assert part != null;
		assert ref != null;
		assert transfer != null;
		_part = part;
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
			IDomainObject<?> dObj = null;
			if ( _transfer instanceof IAccessibleObjectTransfer ) {
				Object obj = ((IAccessibleObjectTransfer)_transfer).nativeToJava( 
						event.currentDataType );
				assert obj instanceof IDomainObject<?>;
				dObj = (IDomainObject<?>)obj;
			}
			if ( dObj == null ) {
				_checkUnique = false;
			}
			else {
				if ( _part.getDisplayedValues().contains( dObj ) ) {
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
				if( _part.getDisplayedValues().contains( dObj ) ) {
					MessageDialog.openError(
							null,
							LouisPlugin.getResourceString( 
									"CollectionPart.UniqueTitle" ), //$NON-NLS-1$
							LouisPlugin.getResourceString( 
									"CollectionPart.UniqueMsg" ) ); //$NON-NLS-1$
					return;
				}
			}
			// ok - add
			_part.addToCollection( dObj );
		}
		else {
			event.detail = DND.DROP_NONE;
		}
	}
}