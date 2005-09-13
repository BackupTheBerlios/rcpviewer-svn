package de.berlios.rcpviewer.transaction.internal;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Collections;
import java.util.Collection;
import java.util.Set;
import java.util.List;

import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.IChange;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.transaction.ITransaction;


/**
 * Represents an object has been removed from a collection of a pojo enlisted 
 * within a {@link ITransaction}
 * 
 * <p>
 * This implementation (like all implementations of {@link de.berlios.rcpviewer.transaction.IChange})
 * has value semantics.
 * 
 */
public final class RemoveFromCollectionChange<V> extends AbstractCollectionChange<V> {


	public RemoveFromCollectionChange(
			final ITransaction transaction,
			final ITransactable transactable,
			final Collection<V> collection,
			final String collectionName,
			final V addedValue) {
		super(transaction, transactable, collection, collectionName, addedValue);
	}

	/*
	 * Remove from the collection.
	 * 
	 * @see de.berlios.rcpviewer.transaction.IChange#execute()
	 */
	public final Object doExecute() {
		getCollection().remove(getReferencedObject());
		return null;
	}

	/*
	 * Re-adds the object from the collection.
	 * 
	 * @see de.berlios.rcpviewer.transaction.IChange#undo()
	 */
	public void undo() {
		getCollection().add(getReferencedObject());
	}


	/*
	 * Value semantics.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(final Object other) {
		return
			sameClass(other) &&
			equals((RemoveFromCollectionChange<V>)other);
	}

	public final boolean equals(final RemoveFromCollectionChange<V> other) {
		return
		    _transactable.equals(other._transactable) &&
			getCollection().equals(other.getCollection()) &&
			getReferencedObject().equals(other.getReferencedObject());
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
		return getDescription() + ": removed " + getReferencedObject(); 
	}
}
