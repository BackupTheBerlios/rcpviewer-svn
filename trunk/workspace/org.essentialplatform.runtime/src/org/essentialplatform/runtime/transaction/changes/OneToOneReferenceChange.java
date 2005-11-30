package org.essentialplatform.runtime.transaction.changes;

import java.lang.reflect.Field;

import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.PojoAlreadyEnlistedException;


/**
 * Represents a change to an 1:1 reference of a pojo enlisted within a
 * {@link ITransaction}
 * 
 * <p>
 * This implementation (like all implementations of {@link org.essentialplatform.runtime.transaction.changes.IChange})
 * has value semantics.
 * 
 */
public final class OneToOneReferenceChange extends AbstractFieldChange {

	private ITransactable _referencedObjOrNull;
	
	public OneToOneReferenceChange(
			final ITransaction transaction,
			final ITransactable transactable,
			final Field field,
			final Object referencedObjOrNull) {
		super(transaction, transactable, field, referencedObjOrNull);
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
		if (!_transactable.equals(other._transactable)) return false;
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
