package org.essentialplatform.runtime.shared.transaction.changes;

import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.transaction.ITransactable;
import org.essentialplatform.runtime.shared.transaction.ITransaction;

/**
 * Represents the deletion of a {@link IDomainObject}.
 * 
 * <p>
 * The change doesn't in fact <i>do</i> anything, however its existence in the
 * change set of a transaction is important because of how it is interpreted
 * by the persistent object store.
 */
public final class DeletionChange extends AbstractChange {

	private static String description(final ITransactable transactable) {
		IPojo pojo = (IPojo)transactable;
		IDomainObject domainObject = pojo.domainObject();
		return "deleted " + domainObject.getDomainClass().getName();
	}
	
	private static Object[] extendedInfo(final ITransactable transactable) {
		return new Object[]{};
	}

	public DeletionChange(final ITransaction transaction, final ITransactable transactable) {
		super(transaction, transactable, description(transactable), extendedInfo(transactable), false);
	}
	
	/**
	 * For testing of comparators only
	 */
	public DeletionChange() {
	}

	/*
	 * Instructs the pojo's wrapping domain object that it is now transient.
	 *  
	 * @see org.essentialplatform.transaction.IChange#doExecute()
	 */
	@Override
	public final Object doExecute() {
		IPojo pojo = (IPojo)getInitiatingPojo();
		IDomainObject<?> domainObject = pojo.domainObject();
		if (domainObject != null) {
			domainObject.nowTransient();
		}
		return pojo;
	}

	/*
	 * 
	 * @see org.essentialplatform.transaction.IChange#undo()
	 */
	public void doUndo() {
		IPojo pojo = (IPojo)getInitiatingPojo();
		IDomainObject<?> domainObject = pojo.domainObject();
		if (domainObject != null) {
			domainObject.nowPersisted();
		}
	}

	/*
	 * Cannot be undone.
	 * 
	 * @see org.essentialplatform.transaction.IChange#canUndo()
	 */
	public boolean canUndo() {
		return false;
	}


	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(final Object other) {
		return
			sameClass(other) &&
			equals((DeletionChange)other);
	}

	/**
	 * Typesafe overload of {@link #equals(Object)}.
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(final DeletionChange other) {
		return getInitiatingPojo().equals(other.getInitiatingPojo());
	}

	/*
	 * Since we want value semantics we must provide a hashCode(), however 
	 * as there is nothing we can use to construct a meaningful hashCode()
	 * we simply return 1.
	 * 
	 * <p>
	 * This will have some performance implications, but in general the 
	 * number of changes in a set is very small.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 1;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "deleted " + getDescription();
	}
	
}
