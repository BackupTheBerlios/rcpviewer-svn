package org.essentialplatform.transaction.internal;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Collections;
import java.util.Set;
import java.util.List;

import org.essentialplatform.transaction.ITransactable;
import org.essentialplatform.transaction.IChange;
import org.essentialplatform.transaction.PojoAlreadyEnlistedException;
import org.essentialplatform.session.IDomainObject;
import org.essentialplatform.transaction.ITransaction;


/**
 * Represents an object has been added to a collection of a pojo enlisted 
 * within a {@link ITransaction}
 * 
 * <p>
 * This implementation (like all implementations of {@link org.essentialplatform.transaction.IChange})
 * has value semantics.
 * 
 */
public final class AddToCollectionChange<V> extends AbstractCollectionChange<V> {

	public AddToCollectionChange(
			final ITransaction transaction,
			final ITransactable transactable,
			final Collection<V> collection,
			final String collectionName,
			final V addedValue) {
		super(transaction, transactable, collection, collectionName, addedValue);
	}

	/*
	 * Adds the referenced object to the collection.
	 * 
	 * @see org.essentialplatform.transaction.IChange#doExecute()
	 */
	@Override
	public final Object doExecute() {
		getCollection().add(getReferencedObject());
		return null;
	}

	/*
	 * Removes the referenced object from the collection.
	 * 
	 * @see org.essentialplatform.transaction.IChange#undo()
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
		return getDescription() + ": added " + getReferencedObject(); 
	}


}
