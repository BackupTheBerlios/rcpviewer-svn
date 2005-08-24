package de.berlios.rcpviewer.transaction.internal;

import java.lang.reflect.Field;
import java.util.List;

import de.berlios.rcpviewer.transaction.ITransactable;
import java.util.Set;

/**
 * Represents the instantiation of a {@link IDomainObject}.
 */
public final class InstantiationChange extends AbstractChange {

	private static String description(final ITransactable transactable) {
		throw new RuntimeException("Operation not yet implemented.");
	}
	
	private static Object[] extendedInfo(final ITransactable transactable) {
		throw new RuntimeException("Operation not yet implemented.");
	}

	private final ITransactable _transactable;
	public InstantiationChange(final ITransactable transactable) {
		super(description(transactable), extendedInfo(transactable), false);
		_transactable = transactable;
	}
	
	/*
	 * 
	 * @see de.berlios.rcpviewer.transaction.IChange#execute()
	 */
	public void execute() {
		throw new RuntimeException("Operation not yet implemented.");
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.transaction.IChange#undo()
	 */
	public void undo() {
		throw new RuntimeException("Operation not yet implemented.");
	}

	/*
	 * Cannot be undone.
	 * 
	 * @see de.berlios.rcpviewer.transaction.IChange#canUndo()
	 */
	public boolean canUndo() {
		return false;
	}


	/*
	 * @see de.berlios.rcpviewer.transaction.IChange#getModifiedPojos()
	 */
	public Set<ITransactable> getModifiedPojos() {
		throw new RuntimeException("Operation not yet implemented.");	
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
	 * TODO: should hash on more than this...
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return _transactable.hashCode();
	}

}
