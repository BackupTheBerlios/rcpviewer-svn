package de.berlios.rcpviewer.transaction;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * An immutable chain or sequence of {@link IChange}s.
 * 
 * <p>
 * Composite pattern, with value type semantics.
 */
public final class ChangeSet implements IChange {

	private final IChange[] _changes;
	private final String _asString;
	private final String _description;
	private final Object[] _extendedInfo;
	private final boolean _irreversible;
	
	/**
	 * Construct the chain from a list of {@link IChange}s.
	 * 
	 * <p>
	 * The changes in the copied out of the list; subsequent changes to the 
	 * list's contents are not reflected in this set. 
	 *  
	 * @param list
	 */
	public ChangeSet(final List<IChange> list) {
		this(list.toArray(new IChange[]{}));
	}
	public ChangeSet(final IChange[] changes) {
		_description = changes.length + " changes";
		_extendedInfo = new Object[]{};
		_irreversible = calculateIfIrreversible(changes);
		
		// changes
		_changes = new IChange[changes.length];
		System.arraycopy(changes, 0, _changes, 0, changes.length);
		
		// string
		_asString = calculateAsString(changes);
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
		for (int i = 0; i < changes.length; i++) {
			buf.append(changes[i].toString());
			if (i != changes.length-1) {
				buf.append(", ");
			}
		}
		return buf.toString();
	}


	/*
	 * @see de.berlios.rcpviewer.transaction.IChange#getDescription()
	 */
	public final String getDescription() {
		return _description;
	}
	/*
	 * @see de.berlios.rcpviewer.transaction.IChange#getExtendedInfo()
	 */
	public final Object[] getExtendedInfo() {
		return _extendedInfo;
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.IChange#isIrreversible()
	 */
	public final boolean isIrreversible() {
		return _irreversible;
	}


	/*
	 * Executes each of the changes in turn.
	 * 
	 * @see de.berlios.rcpviewer.transaction.IChange#execute()
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
	 * @see de.berlios.rcpviewer.transaction.IChange#undo()
	 */
	public void undo() {
		if (isIrreversible()) {
			throw new IrreversibleTransactionException();
		}
		for (int i = 0; i < _changes.length; i++) {
			_changes[_changes.length-i-1].undo();
		}
	}


	/**
	 * The number of atoms in this chain.
	 * 
	 * @return
	 */
	public int size() {
		return _changes.length;
	}
	
	/**
	 * Obtain the nth atom.
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
	 * @see de.berlios.rcpviewer.transaction.IChange#getModifiedPojos()
	 */
	public Set<ITransactable> getModifiedPojos() {
		Set<ITransactable> pojos = new HashSet<ITransactable>();
		for (int i = 0; i < _changes.length; i++) {
			pojos.addAll(_changes[i].getModifiedPojos());
		}
		return Collections.unmodifiableSet(pojos);
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
