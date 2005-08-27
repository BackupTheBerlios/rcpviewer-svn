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
 * Represents a change to an 1:1 reference of a pojo enlisted within a
 * {@link ITransaction}
 * 
 * <p>
 * This implementation (like all implementations of {@link de.berlios.rcpviewer.transaction.IChange})
 * has value semantics.
 * 
 */
public final class OneToOneReferenceChange extends AbstractFieldChange {

	public OneToOneReferenceChange(
			final ITransactable transactable,
			final Field field,
			final Object referencedObjOrNull) {
		super(transactable, field, referencedObjOrNull);
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
		return
		    _transactable.equals(other._transactable) &&
			getField().equals(other.getField()) &&
			getPreValue().equals(other.getPreValue()) &&
			getPostValue().equals(other.getPostValue());
	}

	/**
	 * TODO: should hash on all values.
	 */
	@Override
	public int hashCode() {
		return _transactable.hashCode();
	}

}
