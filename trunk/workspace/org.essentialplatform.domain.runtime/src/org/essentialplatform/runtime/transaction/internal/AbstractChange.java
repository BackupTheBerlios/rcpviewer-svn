package org.essentialplatform.runtime.transaction.internal;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

import sun.rmi.runtime.GetThreadPoolAction;

import org.essentialplatform.runtime.session.IDomainObject;
import org.essentialplatform.runtime.session.IPojo;
import org.essentialplatform.runtime.transaction.IChange;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.PojoAlreadyEnlistedException;


/**
 * Convenience adapter to simplify the implementation of {@link IChange}s.
 */
public abstract class AbstractChange implements IChange {

	private static final Object[] __EMPTY_OBJECT_ARRAY = new Object[]{};
	
	protected final ITransaction _transaction;
	protected final ITransactable _transactable;
	private final String _description;
	private final Object[] _extendedInfo;
	private final boolean _irreversible;

	private final Set<ITransactable> _transactableAsSet = new HashSet<ITransactable>();

	protected AbstractChange(final ITransaction transaction, final ITransactable transactable, final String description, final Object[] extendedInfo, final boolean irreversible) {
		_transaction = transaction;
		_transactable = transactable;
		_description = description;
		_extendedInfo = extendedInfo == null? __EMPTY_OBJECT_ARRAY: extendedInfo;
		_irreversible = irreversible;
		modifies(_transactable); // the transactable is always in the modifiedPojos set.
	}
	
	/**
	 * The provided {@link ITransactable} will be included in the set returned
	 * by {@link #getModifiedPojos()}.
	 * 
	 * @param transactable
	 */
	protected final void modifies(final ITransactable transactable) {
		_transactableAsSet.add(transactable);
	}

	
	/*
	 * @see org.essentialplatform.transaction.IChange#getModifiedPojos()
	 */
	public Set<ITransactable> getModifiedPojos() {
		return Collections.unmodifiableSet(_transactableAsSet);
	}

	/*
	 * @see org.essentialplatform.transaction.IChange#getDescription()
	 */
	public final String getDescription() {
		return _description;
	}
	/*
	 * @see org.essentialplatform.transaction.IChange#getExtendedInfo()
	 */
	public final Object[] getExtendedInfo() {
		return _extendedInfo;
	}

	/*
	 * @see org.essentialplatform.transaction.IChange#isIrreversible()
	 */
	public final boolean isIrreversible() {
		return _irreversible;
	}

	/*
	 * Template pattern; see <code>doExecute()</code>
	 *
	 * @see #doExecute
	 * @see org.essentialplatform.transaction.IChange#execute()
	 */
	public final Object execute() throws PojoAlreadyEnlistedException {
		IPojo pojo = (IPojo)_transactable;
		IDomainObject<?> domainObject = pojo.getDomainObject();
		// only if we have a domain object (ie fully instantiated) and
		// are attached to a session do we check.
		if (domainObject != null && domainObject.isAttached()) {
			if (_transaction.isInState(ITransaction.State.BUILDING_CHANGE, ITransaction.State.IN_PROGRESS)) {
				if (!_transaction.addingToInteractionChangeSet(this)) {
					throw new PojoAlreadyEnlistedException();			
				}
			}
		}
		return doExecute();
	}
	
	protected abstract Object doExecute();


	/*
	 * force subclasses to implement
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public abstract int hashCode();

	/*
	 * force subclasses to implement
	 *  
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public abstract boolean equals(final Object o);
	
	protected final boolean sameClass(final Object other) {
		return getClass().equals(other.getClass());
	}


}
