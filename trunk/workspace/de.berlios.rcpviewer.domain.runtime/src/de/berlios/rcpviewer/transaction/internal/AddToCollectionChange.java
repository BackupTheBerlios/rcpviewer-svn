package de.berlios.rcpviewer.transaction.internal;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Collections;
import java.util.Set;
import java.util.List;

import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.IChange;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.transaction.ITransaction;


/**
 * Represents an object has been added to a collection of a pojo enlisted 
 * within a {@link ITransaction}
 * 
 * <p>
 * This implementation (like all implementations of {@link de.berlios.rcpviewer.transaction.IChange})
 * has value semantics.
 * 
 */
public final class AddToCollectionChange<V> extends AbstractCollectionChange {

	public AddToCollectionChange(
			final ITransactable transactable,
			final Field field,
			final V addedValue) {
		super(transactable, field, addedValue);
	}

	/*
	 * Adds the referenced object to the collection.
	 * 
	 * @see de.berlios.rcpviewer.transaction.IChange#execute()
	 */
	public void execute() {
		getCollection().add(getReferencedObject());
	}

	/*
	 * Removes the referenced object from the collection.
	 * 
	 * @see de.berlios.rcpviewer.transaction.IChange#undo()
	 */
	public void undo() {
		getCollection().remove(getReferencedObject());
	}


	/*
	 * Value semantics.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(final Object other) {
		return
			sameClass(other) &&
			equals((AddToCollectionChange<V>)other);
	}

	public final boolean equals(final AddToCollectionChange<V> other) {
		return
		    _transactable.equals(other._transactable) &&
			getField().equals(other.getField()) &&
			getReferencedObject().equals(other.getReferencedObject());
	}

	/**
	 * TODO: should hash on all values.
	 */
	@Override
	public int hashCode() {
		return _transactable.hashCode();
	}

	public String toString() {
		return getField().getName() + ": added " + getReferencedObject(); 
	}


}
