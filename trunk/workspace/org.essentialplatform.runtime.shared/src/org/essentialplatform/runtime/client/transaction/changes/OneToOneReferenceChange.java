package org.essentialplatform.runtime.client.transaction.changes;

import java.lang.reflect.Field;

import org.essentialplatform.runtime.client.domain.bindings.IObjectOneToOneReferenceClientBinding;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOneToOneReference;


/**
 * Represents a change to an 1:1 reference of a pojo enlisted within a
 * {@link ITransaction}
 * 
 * <p>
 * This implementation (like all implementations of {@link org.essentialplatform.runtime.client.transaction.changes.IChange})
 * has value semantics.
 * 
 */
public final class OneToOneReferenceChange extends AbstractFieldChange {

	/**
	 * <tt>transient</tt> for serialization.
	 */
	private transient IObjectOneToOneReference _reference;
	private IPojo _referencedObjOrNull;
	
	public OneToOneReferenceChange(
			final ITransaction transaction,
			final IPojo transactable,
			final Field field,
			final Object referencedObjOrNull,
			final IObjectOneToOneReference reference) {
		super(transaction, transactable, field, referencedObjOrNull);
		_reference = reference;
	}

	/**
	 * For testing of comparators only
	 */
	public OneToOneReferenceChange() {
	}

	@Override
	protected void notifyListeners(boolean execute) {
		if (_reference!= null) {
			IObjectOneToOneReferenceClientBinding refBinding = (IObjectOneToOneReferenceClientBinding)_reference.getBinding();
			refBinding.set(execute?getPostValue():getPreValue());
		}
	}

	/*
	 * Value semantics.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(final Object other) {
		return
			sameClass(other) &&
			equals((OneToOneReferenceChange)other);
	}

	public final boolean equals(final OneToOneReferenceChange other) {
		if (other == null) return false;
		if (!getInitiatingPojo().equals(other.getInitiatingPojo())) return false;
		if (!getField().equals(other.getField())) return false;
		boolean preValuesMatch = 
			getPreValue() == null && other.getPreValue() == null ||
			getPreValue().equals(other.getPreValue());
		boolean postValuesMatch = 
			getPostValue() == null && other.getPostValue() == null ||
			getPostValue().equals(other.getPostValue());
		return preValuesMatch && postValuesMatch;
	}

	/*
	 * Since we want value semantics we must provide a hashCode(); the best 
	 * we can do is use a hash code based on the field.
	 */
	@Override
	public int hashCode() {
		return getField().hashCode();
	}

}
