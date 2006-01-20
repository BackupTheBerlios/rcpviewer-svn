package org.essentialplatform.runtime.shared.transaction.changes;

import java.lang.reflect.Field;

import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.transaction.ITransactable;
import org.essentialplatform.runtime.shared.transaction.ITransaction;


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
 * (say) {@link IDomainObject.IObjectReference}s.  That's because AspectJ is picking
 * up modifications to fields (instance variables) rather than invokations of
 * mutators.  Although it might seem that this code is somewhat ugly, it does
 * mean that there are no restrictions on the domain programmer (in particular, we
 * don't insist that an object changing its own state must do so by using its own
 * mutators).
 */
public abstract class AbstractFieldChange extends AbstractChange implements IModificationChange {

	/**
	 * The attribute that is being modified, accessed through 
	 * {@link #getAttribute()}.
	 * 
	 * <tt>transient</tt> since not required to be distributed.
	 */
	private transient Field _field;
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


	protected static String description(final Field field) {
		return "changed " + field.getName();
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
	 * Provided only for comparator testing.
	 */
	public AbstractFieldChange() {
		_preValue = _postValue = null;
	}

	/**
	 * Captures the current value of the attribute as the 
	 * {@link #getPreValue()}.
	 * 
	 * @param transaction - the transaction to which this change is added
	 * @param transactable
	 * @param field
	 * @param postValue
	 */
	public AbstractFieldChange(
			final ITransaction transaction,
			final ITransactable transactable,
			final Field field,
			final Object postValue) {
		super(transaction, transactable, description(field), extendedInfo(transactable, field, postValue), false);
		this._field = field;
		try {
			field.setAccessible(true);
			this._preValue = field.get(transactable);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		_postValue = postValue;
		if (postValue instanceof ITransactable) {
			modifies((ITransactable)postValue);	
		}
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
	 * Default implementation returns <code>false</code> indicating that the
	 * change DOES do something.
	 * 
	 * @see org.essentialplatform.runtime.transaction.changes.IChange#doesNothing()
	 */
	public boolean doesNothing() {
		return getPreValue() == null && getPostValue() == null ||
		       getPreValue().equals(getPostValue());
	}


	/*
	 * Sets the value of the field to its post value.
	 * 
	 * @see org.essentialplatform.transaction.IChange#doExecute()
	 */
	@Override
	public final Object doExecute()  {
		try {
			_field.set(getInitiatingPojo(), _postValue);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		};
		return null;
	}

	/*
	 * Sets the value of the field to its pre value.
	 * 
	 * @see org.essentialplatform.transaction.IChange#undo()
	 */
	public void doUndo() {
		try {
			_field.set(getInitiatingPojo(), _preValue);
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
