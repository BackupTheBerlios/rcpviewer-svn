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
 * Represents a change to a field (attribute or 1:1 reference) of a pojo 
 * enlisted within a {@link ITransaction}
 * 
 * <p>
 * Although implementations of IChange are expected to have value semantics, 
 * these are not implemented here since this is abstract; instead subclasees
 * should implement themselves.
 *  
 * <p>
 * Note that this works on {@link java.lang.reflect.Field}s, rather than
 * (say) {@link IDomainObject.IReference}s.  That's because AspectJ is picking
 * up modifications to fields (instance variables) rather than invokations of
 * mutators.  Although it might seem that this code is somewhat ugly, it does
 * mean that there are no restrictions on the domain programmer (in particular, we
 * don't insist that an object changing its own state must do so by using its own
 * mutators).
 */
public abstract class AbstractFieldChange extends AbstractChange {

	protected static String description(final Field field) {
		return field.getName();
	}
	
	protected static Object[] extendedInfo(final ITransactable transactable, final Field field, final Object postValue) {
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

	/**
	 * Protected for implementation of equals etc in subclasses.
	 */
	protected final ITransactable _transactable;
	private final Set<ITransactable> _transactableAsSet;
	
	/**
	 * CAptures the current value of the attribute as the 
	 * {@link #getPreValue()}.
	 * 
	 * @param attribute
	 * @param postValue
	 */
	public AbstractFieldChange(
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
		Set<ITransactable> transactableAsSet = new HashSet<ITransactable>();
		transactableAsSet.add(transactable);
		_transactableAsSet = Collections.unmodifiableSet(transactableAsSet);
		_postValue = postValue;
	}

	/*
	 * Consists of just the object whose field is being modified.
	 *  
	 * @see de.berlios.rcpviewer.transaction.IChange#getModifiedPojos()
	 */
	public Set<ITransactable> getModifiedPojos() {
		return _transactableAsSet;
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

	public String toString() {
		return getField().getName() + ": " + getPreValue() + "->" + getPostValue(); 
	}


}
