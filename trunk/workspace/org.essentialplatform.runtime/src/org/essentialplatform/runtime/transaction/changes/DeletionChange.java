package org.essentialplatform.runtime.transaction.changes;

import java.lang.reflect.Field;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.PojoAlreadyEnlistedException;

import java.util.Set;

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
		IDomainObject domainObject = pojo.getDomainObject();
		return "deleted " + domainObject.getDomainClass().getName();
	}
	
	private static Object[] extendedInfo(final ITransactable transactable) {
		return new Object[]{};
	}

	public DeletionChange(final ITransaction transaction, final ITransactable transactable) {
		super(transaction, transactable, description(transactable), extendedInfo(transactable), false);
	}
	
	/*
	 * Instructs the pojo's wrapping domain object that it is now transient.
	 *  
	 * @see org.essentialplatform.transaction.IChange#doExecute()
	 */
	@Override
	public final Object doExecute() {
		IPojo pojo = (IPojo)_transactable;
		IDomainObject<?> domainObject = pojo.getDomainObject();
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
		IPojo pojo = (IPojo)_transactable;
		IDomainObject<?> domainObject = pojo.getDomainObject();
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
		return _transactable.equals(other._transactable);
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
