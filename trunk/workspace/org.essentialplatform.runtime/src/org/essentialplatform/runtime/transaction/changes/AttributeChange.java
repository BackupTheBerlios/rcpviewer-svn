package org.essentialplatform.runtime.transaction.changes;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Collections;
import java.util.Set;
import java.util.List;

import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IDomainObject.IObjectAttribute;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.omg.CORBA._PolicyStub;


/**
 * Represents a change to an attribute of a pojo enlisted within a
 * {@link ITransaction}
 * 
 * <p>
 * This implementation (like all implementations of {@link org.essentialplatform.runtime.transaction.changes.IChange})
 * has value semantics.
 * 
 */
public final class AttributeChange extends AbstractFieldChange {

	/**
	 * <tt>transient</tt> for serialization.
	 */
	private transient IObjectAttribute _attribute;

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
			final Object postValue, 
			final IDomainObject.IObjectAttribute attribute) {
		super(transaction, transactable, field, postValue);
		_attribute = attribute;
	}

	/**
	 * For testing of comparators only
	 */
	public AttributeChange() {
	}

	@Override
	protected void notifyListeners(boolean execute) {
		if (_attribute != null) {
			_attribute.notifyListeners(execute?getPostValue():getPreValue());
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
			equals((AttributeChange)other);
	}

	public final boolean equals(final AttributeChange other) {
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
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getField().hashCode();
	}

}
