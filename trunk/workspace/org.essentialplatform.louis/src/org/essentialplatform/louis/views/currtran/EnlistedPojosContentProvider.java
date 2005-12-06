/**
 * 
 */
package org.essentialplatform.louis.views.currtran;

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.TransactionManager;
import org.essentialplatform.runtime.transaction.changes.ChangeSet;

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
class EnlistedPojosContentProvider extends AbstractCurrTranContentProvider  {

	/* 
	 * Used to obtain the elements of root (should be the current transaction).
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] doGetElements(Object inputElement) {
		assert inputElement != null && 
		       inputElement instanceof ITransaction;
		if (getCurrentTransaction() == null) {
			return new Object[]{};
		}
		return copyAsArray(getCurrentTransaction().getEnlistedPojos());	
	}

}
