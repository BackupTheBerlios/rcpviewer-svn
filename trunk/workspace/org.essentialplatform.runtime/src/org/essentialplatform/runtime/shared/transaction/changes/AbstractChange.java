package org.essentialplatform.runtime.shared.transaction.changes;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

import sun.rmi.runtime.GetThreadPoolAction;

import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.transaction.ITransactable;
import org.essentialplatform.runtime.shared.transaction.ITransaction;
import org.essentialplatform.runtime.shared.transaction.PojoAlreadyEnlistedException;
import org.essentialplatform.runtime.shared.transaction.changes.IChange.IVisitor;


/**
 * Convenience adapter to simplify the implementation of {@link IChange}s.
 * 
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

	/**
	 * The object on which this change was initiated (ie as returned by
	 * {@link #getInitiatingPojo()}).
	 */
	private transient ITransactable _initiatingPojo;
	/**
	 * The object on which the change was initiated.
	 * 
	 * <p>
	 * The backing field reference for this is <tt>transient</tt> so that it 
	 * is not included in any serialization.   If a subclass change requires 
	 * the transactable object to be serialized (specifically, we mean  
	 * {@link InstantiationChange}), then it should store a reference to the
	 * transactable itself.
	 *  
	 * @return
	 */
	public ITransactable getInitiatingPojo() {
		return _initiatingPojo;
	}

	private IDomainObject<?> _initiatingPojoDO;
	/*
	 * @see org.essentialplatform.runtime.transaction.changes.IChange#getInitiatingPojoDO()
	 */
	public IDomainObject<?> getInitiatingPojoDO() {
		return _initiatingPojoDO;
	}

	/**
	 * <tt>transient</tt> for serialization.
	 */
	private transient Set<ITransactable> _transactableAsSet = new HashSet<ITransactable>();

	/**
	 * 
	 * @param transaction
	 * @param transactable - the object on which the change was initiated.
	 * @param description
	 * @param extendedInfo
	 * @param irreversible
	 */
	protected AbstractChange(final ITransaction transaction, final ITransactable transactable, final String description, final Object[] extendedInfo, final boolean irreversible) {
		_transaction = transaction;
		_initiatingPojo = transactable;
		_initiatingPojoDO = ((IPojo)_initiatingPojo).domainObject();
		_description = description;
		_extendedInfo = extendedInfo == null? __EMPTY_OBJECT_ARRAY: extendedInfo;
		_irreversible = irreversible;
		modifies(transactable); // the transactable is always in the modifiedPojos set.
	}
	
	/**
	 * For testing of comparators only.
	 *
	 */
	protected AbstractChange() {
		_description = null;
		_extendedInfo = null;
		_irreversible = false;
		_initiatingPojoDO = null;
		_transaction = null;
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
		if (_initiatingPojoDO != null && _initiatingPojoDO.isAttached()) {
			if (_transaction.isInState(ITransaction.State.BUILDING_CHANGE, ITransaction.State.IN_PROGRESS)) {
				if (!_transaction.addingToInteraction(this)) {
					throw new PojoAlreadyEnlistedException();			
				}
			}
		}
		Object retval = doExecute();

		notifyListeners(true);
		_initiatingPojoDO.externalStateChanged();

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
		_initiatingPojoDO.externalStateChanged();
	}
	
	protected abstract void doUndo();


	
	/*
	 * Invokes {@link IVisitor#visit(IChange)}, as per the general contract.
	 *
	 * <p>
	 * Subclasses that are composites should override this to additionally
	 * ensure that all contained components accept the visitor, as well as
	 * having the visitor accept themselves.
	 * 
	 * @see org.essentialplatform.runtime.transaction.changes.IChange#accept(org.essentialplatform.runtime.transaction.changes.IChange.IVisitor)
	 */
	public void accept(IVisitor visitor) {
		visitor.visit(this);
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
