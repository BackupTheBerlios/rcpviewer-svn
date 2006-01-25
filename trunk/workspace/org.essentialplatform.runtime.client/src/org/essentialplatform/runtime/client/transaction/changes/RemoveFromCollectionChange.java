package org.essentialplatform.runtime.client.transaction.changes;

import java.util.Collection;

import org.essentialplatform.runtime.client.domain.bindings.IObjectCollectionReferenceClientBinding;
import org.essentialplatform.runtime.client.transaction.ITransactable;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.shared.domain.IDomainObject;


/**
 * Represents an object has been removed from a collection of a pojo enlisted 
 * within a {@link ITransaction}
 * 
 * <p>
 * This implementation (like all implementations of {@link org.essentialplatform.runtime.client.transaction.changes.IChange})
 * has value semantics.
 * 
 */
public final class RemoveFromCollectionChange<V> extends AbstractCollectionChange<V> {


	public RemoveFromCollectionChange(
			final ITransaction transaction,
			final ITransactable transactable,
			final Collection<V> collection,
			final V addedValue, 
			final IDomainObject.IObjectCollectionReference reference) {
		super(transaction, transactable, collection, addedValue, reference);
	}

	/**
	 * For testing of comparators only
	 */
	public RemoveFromCollectionChange() {
	}

	/*
	 * Remove from the collection.
	 * 
	 * @see org.essentialplatform.transaction.IChange#execute()
	 */
	public final Object doExecute() {
		getCollection().remove(getReferencedObject());
		return null;
	}

	protected void notifyListeners(final boolean execute) {
		boolean beingAdded = !execute;
		if (_reference != null) {
			IObjectCollectionReferenceClientBinding refBinding = (IObjectCollectionReferenceClientBinding)_reference.getBinding(); 
			refBinding.notifyListeners((Object)_referencedObject, beingAdded);
		}
	}

	/*
	 * Re-adds the object from the collection.
	 * 
	 * @see org.essentialplatform.transaction.IChange#undo()
	 */
	public void doUndo() {
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
		if (other == null) return false;
		if (!getInitiatingPojo().equals(other.getInitiatingPojo())) return false;
		if (!getCollection().equals(other.getCollection())) return false;
		boolean referencedObjectMatch = 
			getReferencedObject() == null && other.getReferencedObject() == null ||
			getReferencedObject().equals(other.getReferencedObject());
		return referencedObjectMatch;
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
