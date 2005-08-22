package de.berlios.rcpviewer.transaction.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
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
 * <p>
 * Note that this works on {@link java.lang.reflect.Field}s, rather than
 * (say) {@link IDomainObject.IAttribute}s.  That's because AspectJ is picking
 * up modifications to fields (instance variables) rather than invokations of
 * mutators.  Although it might seem that this code is somewhat ugly, it does
 * mean that there are no restrictions on the domain programmer (in particular, we
 * don't insist that an object changing its own state must do so by using its own
 * mutators).
 */
public final class AttributeChange extends AbstractChange {

	private static String description(final Field field) {
		return field.getName();
	}
	
	private static Object[] extendedInfo(final ITransactable transactable, final Field field, final Object postValue) {
		field.setAccessible(true);
		String preValueStr = "pre: ";
		try {
			preValueStr += "'" + field.get(transactable) + "'";
		} catch (IllegalArgumentException ex) {
			preValueStr += "???";
		} catch (IllegalAccessException ex) {
			preValueStr += "???";
		} 
		String postValueStr = "post: '" + postValue; 
		return new Object[]{preValueStr, postValueStr};
	}
	/**
	 * The attribute that is being modified, accessed through 
	 * {@link #getAttribute()}.
	 */
	private final Field _field;
	/**
	 * The value of the {@link #getField()} before it was modified, accessed
	 * through {@link #getPreValue()}.
	 */
	private final Object _preValue;
	/**
	 * The value of the {@link #getField()} after it was modified, accessed
	 * through {@link #getPostValue()}.
	 */
	private final Object _postValue;

	private final ITransactable _transactable;
	private final List<ITransactable> _transactableAsList;
	
	/**
	 * CAptures the current value of the attribute as the 
	 * {@link #getPreValue()}.
	 * 
	 * @param attribute
	 * @param postValue
	 */
	public AttributeChange(
			final ITransactable transactable,
			final Field field,
			final Object postValue) {
		super(description(field), extendedInfo(transactable, field, postValue), false);
		this._field = field;
		try {
			field.setAccessible(true);
			this._preValue = field.get(transactable);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		_transactable = transactable;
		List<ITransactable> transactableAsList = new ArrayList<ITransactable>();
		transactableAsList.add(transactable);
		_transactableAsList = Collections.unmodifiableList(transactableAsList);
		_postValue = postValue;
	}

	/*
	 * Consists of just the object whose field is being modified.
	 *  
	 * @see de.berlios.rcpviewer.transaction.IChange#getModifiedPojos()
	 */
	public List<ITransactable> getModifiedPojos() {
		return _transactableAsList;
	}

	/**
	 * The field that is being modified.
	 * 
	 * @return
	 */
	public Field getField() {
		return _field;
	}

	/**
	 * The value of the field <i>before</i> modification.
	 * 
	 * @return
	 */
	public Object getPreValue() {
		return _preValue;
	}

	/**
	 * The value of the field <i>after</i> modification.
	 * 
	 * @return
	 */
	public Object getPostValue() {
		return _postValue;
	}

	/*
	 * Sets the value of the field to its post value.
	 * 
	 * @see de.berlios.rcpviewer.transaction.IChange#execute()
	 */
	public void execute() {
		try {
			_field.set(_transactable, _postValue);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		};
	}

	/*
	 * Sets the value of the field to its pre value.
	 * 
	 * @see de.berlios.rcpviewer.transaction.IChange#undo()
	 */
	public void undo() {
		try {
			_field.set(_transactable, _preValue);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
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
		return
		    _transactable.equals(other._transactable) &&
			_field.equals(other._field) &&
			_preValue.equals(other._preValue) &&
			_postValue.equals(other._postValue);
	}

	/**
	 * TODO: should hash on all values.
	 */
	@Override
	public int hashCode() {
		return _transactable.hashCode();
	}

	public String toString() {
		return getField().getName() + ": " + getPreValue() + "->" + getPostValue(); 
	}


}
