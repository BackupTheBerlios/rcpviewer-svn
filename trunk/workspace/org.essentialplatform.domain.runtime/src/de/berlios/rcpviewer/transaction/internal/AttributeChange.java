package de.berlios.rcpviewer.transaction.internal;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Collections;
import java.util.Set;
import java.util.List;

import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.IChange;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.transaction.ITransaction;


/**
 * Represents a change to an attribute of a pojo enlisted within a
 * {@link ITransaction}
 * 
 * <p>
 * This implementation (like all implementations of {@link de.berlios.rcpviewer.transaction.IChange})
 * has value semantics.
 * 
 */
public final class AttributeChange extends AbstractFieldChange {

	/**
	 * CAptures the current value of the attribute as the 
	 * {@link #getPreValue()}.
	 * 
	 * @param attribute
	 * @param postValue
	 */
	public AttributeChange(
			final ITransaction transaction,
			final ITransactable transactable,
			final Field field,
			final Object postValue) {
		super(transaction, transactable, field, postValue);
	}

	/*
	 * Value semantics.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(final Object other) {
		return
			sameClass(other) &&
			equals((AttributeChange)other);
	}

	public final boolean equals(final AttributeChange other) {
		return
		    _transactable.equals(other._transactable) &&
			getField().equals(other.getField()) &&
			getPreValue().equals(other.getPreValue()) &&
			getPostValue().equals(other.getPostValue());
	}

	/*
	 * Since we want value semantics we must provide a hashCode(); the best 
	 * we can do is use a hash code based on the field.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getField().hashCode();
	}

}
