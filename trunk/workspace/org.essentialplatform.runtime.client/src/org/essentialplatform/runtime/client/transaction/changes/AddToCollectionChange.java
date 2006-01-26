package org.essentialplatform.runtime.client.transaction.changes;

import java.util.Collection;

import org.essentialplatform.runtime.client.domain.bindings.IObjectCollectionReferenceClientBinding;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;


/**
 * Represents an object has been added to a collection of a pojo enlisted 
 * within a {@link ITransaction}
 * 
 * <p>
 * This implementation (like all implementations of {@link org.essentialplatform.runtime.client.transaction.changes.IChange})
 * has value semantics.
 * 
 */
public final class AddToCollectionChange<V> extends AbstractCollectionChange<V> {

	public AddToCollectionChange(
			final ITransaction transaction,
			final IPojo transactable,
			final Collection<V> collection,
			final V addedValue, 
			final IDomainObject.IObjectCollectionReference reference) {
		super(transaction, transactable, collection, addedValue, reference);
	}

	/**
	 * For testing of comparators only
	 */
	public AddToCollectionChange() {
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
	
	protected void notifyListeners(final boolean execute) {
		boolean beingAdded = execute;
		if (_reference != null) {
			IObjectCollectionReferenceClientBinding refBinding = (IObjectCollectionReferenceClientBinding)_reference.getBinding();
			refBinding.notifyListeners((Object)_referencedObject, beingAdded);
		}
	}



	/*
	 * Removes the referenced object from the collection.
	 * 
	 */
	public final void doUndo() {
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
		return getDescription() + ": added " + getReferencedObject(); 
	}


}
