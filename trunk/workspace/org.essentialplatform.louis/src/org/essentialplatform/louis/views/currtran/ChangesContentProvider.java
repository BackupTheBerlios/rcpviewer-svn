/**
 * 
 */
package org.essentialplatform.louis.views.currtran;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.essentialplatform.runtime.transaction.changes.ChangeSet;
import org.essentialplatform.runtime.transaction.changes.IChange;

/**
 * Returns either undoable or redoable changes of an in-progress transaction, 
 * depending on how it has been instantiated.
 * 
 * <p>
 * The mode as passed into the constructor determines what is represented as
 * the content:
 * <ul>
 * <li> UNDOABLE_CHANGES
 * <li> REDOABLE_CHANGES
 * </ul>
 * 
 * @author Dan Haywood
 */
class ChangesContentProvider extends AbstractCurrTranContentProvider
	implements ITreeContentProvider {

	static enum Mode {
		UNDOABLE_CHANGES,
		REDOABLE_CHANGES,
	}
	
	/**
	 * The mode in which this content provider has been instantiated; used in
	 * {@link #getElements(Object)}.
	 */
	private final Mode _mode;
	
	ChangesContentProvider(final Mode mode) {
		_mode = mode;
	}

	/* 
	 * Used to obtain the elements of root (should be the current transaction).
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] doGetElements(Object inputElement) {
		if (_mode == Mode.UNDOABLE_CHANGES) {
			return copyAsArray(getCurrentTransaction().getUndoableChanges());	
		} else
		if (_mode == Mode.REDOABLE_CHANGES) {
			return copyAsArray(getCurrentTransaction().getRedoableChanges());	
		}
		return new Object[]{};
	}

	/////////////////////////////////////////////////////////////////////
	// ITreeContentProvider
	/////////////////////////////////////////////////////////////////////

	public Object getParent(Object element) {
		if (element == getInput()) {
			// the xactn itself.
			return null;
		}
		if (element instanceof IChange) {
			// return the parent of the change, or the view's input (ie the
			// xactn) otherwise
			IChange change = (IChange)element;
			IChange parent = change.getParent();
			return parent != null?parent:getInput();
		}
		return null;
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement == getInput()) {
			return getElements(parentElement);
		}
		if (parentElement instanceof ChangeSet) {
			ChangeSet changeSet = (ChangeSet)parentElement;
			return changeSet.getChanges();
		}
		// regular IChange
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element == getInput()) {
			return true;
		}
		if (element instanceof ChangeSet) {
			ChangeSet changeSet = (ChangeSet)element;
			return changeSet.size() > 0;
		}
		// regular IChange
		return false;
	}

}
