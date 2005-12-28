package org.essentialplatform.runtime.transaction.changes;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.IrreversibleTransactionException;
import org.essentialplatform.runtime.transaction.changes.IChange.IVisitor;


/**
 * An immutable chain or sequence of {@link IChange}s.
 * 
 * <p>
 * Composite pattern, with value type semantics.
 * 
 * <p>
 * Can be subclassed.
 */
public class ChangeSet implements IChange {

	private final ITransaction _transaction;
	private final IChange[] _changes;
	private final String _asString;
	private final String _description;
	private final Object[] _extendedInfo;
	private final boolean _irreversible;
	private IChange _parent;
	
	/**
	 * Construct the chain from a list of {@link IChange}s.
	 * 
	 * <p>
	 * The changes in the copied out of the list; subsequent changes to the 
	 * list's contents are not reflected in this set. 
	 *  
	 * @param list
	 */
	public ChangeSet(final ITransaction transaction, final List<IChange> list) {
		this(transaction, list.toArray(new IChange[]{}));
	}
	public ChangeSet(final ITransaction transaction, final IChange[] changes) {
		_transaction = transaction;
		_description = changes.length + " change" + (changes.length != 1? "s":""); 
		_extendedInfo = new Object[]{};
		_irreversible = calculateIfIrreversible(changes);
		
		// changes
		_changes = new IChange[changes.length];
		for(int i=0; i<changes.length; i++) {
			_changes[i] = changes[i];
			_changes[i].setParent(this);
		}
		System.arraycopy(changes, 0, _changes, 0, changes.length);
		
		// string
		_asString = calculateAsString(changes);
	}

	/*
	 * @see org.essentialplatform.runtime.transaction.changes.IChange#getTransaction()
	 */
	public ITransaction getTransaction() {
		return _transaction;
	}

	private static boolean calculateIfIrreversible(final IChange[] changes) {
		for (int i = 0; i < changes.length; i++) {
			if (changes[i].isIrreversible()) {
				return true;
			}
		}
		return false;
	}
	private static String calculateAsString(final IChange[] changes) {
		StringBuffer buf = new StringBuffer();
		buf.append(changes.length).append(" change");
		if (changes.length != 1) {
			buf.append("s");
		}
		if (changes.length > 0) {
			buf.append(" (");
		}
		for (int i = 0; i < changes.length; i++) {
			buf.append(changes[i].getDescription());
			if (i < changes.length-1) {
				buf.append(", ");
			}
		}
		if (changes.length > 0) {
			buf.append(")");
		}
		return buf.toString();
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
	 * Executes each of the changes in turn.
	 * 
	 * @see org.essentialplatform.transaction.IChange#execute()
	 */
	public Object execute() {
		for (int i = 0; i < _changes.length; i++) {
			_changes[i].execute();
		}
		return null;
	}
	
	/* 
	 * Undoes each of the changes in turn, in reverse order.
	 * 
	 * @see org.essentialplatform.transaction.IChange#undo()
	 */
	public void undo() {
		if (isIrreversible()) {
			throw new IrreversibleTransactionException();
		}
		for (int i = 0; i < _changes.length; i++) {
			_changes[_changes.length-i-1].undo();
		}
	}


	/*
	 * Invokes {@link IVisitor#visit(IChange)}, as per the general contract.
	 *
	 * <p>
	 * Sunclasses that are composites should override this to ensure that all
	 * contained components instead accept the visitor.
	 * 
	 * @see org.essentialplatform.runtime.transaction.changes.IChange#accept(org.essentialplatform.runtime.transaction.changes.IChange.IVisitor)
	 */
	public void accept(IVisitor visitor) {
		visitor.visit(this);
		for (int i = 0; i < _changes.length; i++) {
			_changes[_changes.length-i-1].accept(visitor);
		}
	}


	
	/**
	 * The number of atoms in this change set.
	 * 
	 * @return
	 */
	public int size() {
		return _changes.length;
	}
	
	/**
	 * Returns a (copy of) the changes referenced by this change set.
	 * 
	 * <p>
	 * Callers are free to modify the returned array as they see fit; it will
	 * have no impact on the change set itself.
	 * @return
	 */
	public IChange[] getChanges() {
		IChange[] changes = new IChange[_changes.length];
		System.arraycopy(_changes, 0, changes, 0, changes.length);
		return _changes;
	}

	/**
	 * Obtain the nth change.
	 * 
	 * @param i, 0-based.
	 * @return
	 */
	public IChange get(final int i) {
		if (i < 0 || i >= size()) {
			throw new IllegalArgumentException("0 < i < size()");
		}
		return _changes[i];
	}

	/*
	 * Simply adds the list of modified pojos from each of the changes.
	 * 
	 * @see org.essentialplatform.transaction.IChange#getModifiedPojos()
	 */
	public Set<ITransactable> getModifiedPojos() {
		Set<ITransactable> pojos = new HashSet<ITransactable>();
		for (IChange change: _changes) {
			pojos.addAll(change.getModifiedPojos());
		}
		return Collections.unmodifiableSet(pojos);
	}

	/*
	 * Just returns <tt>null</tt>.
	 * 
	 * @see org.essentialplatform.runtime.transaction.changes.IChange#getInitiatingPojo()
	 */
	public ITransactable getInitiatingPojo() {
		return null;
	}

	/*
	 * Just returns <tt>null</tt>.
	 * 
	 * @see org.essentialplatform.runtime.transaction.changes.IChange#getInitiatingPojoDO()
	 */
	public IDomainObject getInitiatingPojoDO() {
		return null;
	}


	/**
	 * True iff all changes in the change set return true.
	 */
	public boolean doesNothing() {
		for (IChange change: _changes) {
			if (!change.doesNothing()) return false;
		}
		return true;
	}

	/*
	 * @see org.essentialplatform.runtime.transaction.changes.IChange#getParent()
	 */
	public IChange getParent() {
		return _parent;
	}
	public void setParent(IChange parent) {
		_parent = parent;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return _asString;
	}
	
	/**
	 * Implementation of {@link #equals(Object)} for value type semantics.
	 */
	public boolean equals(Object other) {
		return sameClass(other) && equals((ChangeSet)other);
	}

	private final boolean sameClass(final Object other) {
		return getClass().equals(other.getClass());
	}

	/**
	 * Equal iff the other object is a {@link ChangeSet} with a collection of
	 * {@link IChange}s that are respectively equal according to value 
	 * semantics.
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(final ChangeSet other) {
		if (_changes.length != other._changes.length) {
			return false;
		}
		for (int i = 0; i < _changes.length; i++) {
			if (!_changes[i].equals(other._changes[i])) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * TODO: hash on first 10 in chain? 
	 */
	public int hashCode() {
		return 0;
	}

}
