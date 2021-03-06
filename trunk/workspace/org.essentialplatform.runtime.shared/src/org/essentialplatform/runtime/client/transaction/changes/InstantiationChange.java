package org.essentialplatform.runtime.client.transaction.changes;

import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;

/**
 * Represents the instantiation of a {@link IDomainObject}.
 * 
 * <p>
 * The change doesn't in fact <i>do</i> anything, however its existence in the
 * change set of a transaction is important because of how it is interpreted
 * by the persistent object store.
 */
public final class InstantiationChange extends AbstractChange {

	private static String description(final IPojo pojo) {
		IDomainObject domainObject = pojo.domainObject();
		return "instantiated " + domainObject.getDomainClass().getName();
	}
	
	private static Object[] extendedInfo(final IPojo transactable) {
		return new Object[]{};
	}

	/**
	 * Stored so that the transactable object will be serialized.
	 */
	private IPojo _transactable;

	public InstantiationChange(final ITransaction transaction, final IPojo transactable) {
		super(transaction, transactable, description(transactable), extendedInfo(transactable), false);
		_transactable = transactable;
	}
	
	/**
	 * For testing of comparators only
	 */
	public InstantiationChange() {
	}

	/*
	 * Instructs the pojo's wrapping {@link IDomainObject} that it is now
	 * persisted.
	 *  
	 * @see org.essentialplatform.transaction.IChange#doExecute()
	 */
	@Override
	public final Object doExecute() {
		return _transactable;
	}

	/*
	 * 
	 * @see org.essentialplatform.transaction.IChange#undo()
	 */
	public void doUndo() {
		// TODO: is this required - think more is needed (eg detach from session...)
		if (getInitiatingPojoDO() != null) {
			getInitiatingPojoDO().nowTransient();
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
			equals((InstantiationChange)other);
	}

	/**
	 * Typesafe overload of {@link #equals(Object)}.
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(final InstantiationChange other) {
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
		return "instantiated " + getDescription();
	}
	
}
