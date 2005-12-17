package org.essentialplatform.runtime.transaction.changes;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

import sun.rmi.runtime.GetThreadPoolAction;

import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.PojoAlreadyEnlistedException;


/**
 * Convenience adapter to simplify the implementation of {@link IChange}s.
 */
public abstract class AbstractChange implements IChange {

	private static final Object[] __EMPTY_OBJECT_ARRAY = new Object[]{};
	
	private final String _description;
	private final Object[] _extendedInfo;
	private final boolean _irreversible;

	/**
	 * <tt>transient</tt> for serialization.
	 */
	protected transient final ITransaction _transaction;

	/**
	 * <tt>transient</tt> for serialization.
	 */
	private transient IChange _parent;

	protected ITransactable _transactable;

	/**
	 * Provided as a convenience...
	 * 
	 * <p>
	 * <tt>transient</tt> for serialization.
	 */
	protected transient final IDomainObject<?> _domainObject;

	/**
	 * <tt>transient</tt> for serialization.
	 */
	private transient Set<ITransactable> _transactableAsSet = new HashSet<ITransactable>();

	protected AbstractChange(final ITransaction transaction, final ITransactable transactable, final String description, final Object[] extendedInfo, final boolean irreversible) {
		_transaction = transaction;
		_transactable = transactable;
		_domainObject = ((IPojo)_transactable).domainObject();
		_description = description;
		_extendedInfo = extendedInfo == null? __EMPTY_OBJECT_ARRAY: extendedInfo;
		_irreversible = irreversible;
		modifies(_transactable); // the transactable is always in the modifiedPojos set.
	}
	
	/*
	 * @see org.essentialplatform.runtime.transaction.changes.IChange#getTransaction()
	 */
	public ITransaction getTransaction() {
		return _transaction;
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
	 * @see org.essentialplatform.runtime.transaction.changes.IChange#getParent()
	 */
	public final IChange getParent() {
		return _parent;
	}
	/**
	 * Sets (or unsets if pass in null) the parent for this change.
	 * 
	 * @see #getParent()
	 * @param parent
	 */
	public final void setParent(IChange parent) {
		_parent = parent;
	}

	/*
	 * Template pattern; see <code>doExecute()</code>
	 *
	 * @see #doExecute
	 * @see org.essentialplatform.transaction.IChange#execute()
	 */
	public final Object execute() throws PojoAlreadyEnlistedException {
		// only if we have a domain object (ie fully instantiated) and
		// are attached to a session do we check.
		if (_domainObject != null && _domainObject.isAttached()) {
			if (_transaction.isInState(ITransaction.State.BUILDING_CHANGE, ITransaction.State.IN_PROGRESS)) {
				if (!_transaction.addingToInteractionChangeSet(this)) {
					throw new PojoAlreadyEnlistedException();			
				}
			}
		}
		Object retval = doExecute();

		notifyListeners(true);
		_domainObject.externalStateChanged();

		return retval; 
	}
	
	protected abstract Object doExecute();
	protected void notifyListeners(boolean execute) {}

	/*
	 * Removes the referenced object from the collection.
	 * 
	 * @see org.essentialplatform.transaction.IChange#undo()
	 */
	public final void undo() {
		doUndo();
		notifyListeners(false);
		_domainObject.externalStateChanged();
	}
	
	protected abstract void doUndo();


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


	/*
	 * Default implementation returns <code>false</code> indicating that the
	 * change DOES do something.
	 * 
	 * @see org.essentialplatform.runtime.transaction.changes.IChange#doesNothing()
	 */
	public boolean doesNothing() {
		return false;
	}
	
}
