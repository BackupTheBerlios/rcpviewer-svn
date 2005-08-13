package de.berlios.rcpviewer.transaction.internal.atoms;

import java.lang.reflect.Field;

import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.IWorkAtom;



/**
 * Convenience adapter to simplify the implementation of {@link WorkAtom}s
 * that involve modifications to fields.
 */
public final class FieldModificationWorkAtom extends AbstractWorkAtom {

	private final Field field;
	private final Object preValue;
	private final Object postValue;
	
	public FieldModificationWorkAtom(
			final ITransactable transactable, 
			final Field field, 
			final Object preValue, final Object postValue) {
		
		super(transactable);
		this.field = field;
		this.preValue = preValue;
		this.postValue = postValue;
	}
	
	public void execute() {
		try {
			field.set(getTransactable(), postValue);
		} catch (IllegalArgumentException e) {
			// TODO auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void undo() {
		try {
			field.set(getTransactable(), preValue);
		} catch (IllegalArgumentException e) {
			// TODO auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final boolean equals(final Object other) {
		return
			sameClass(other) &&
			equals((FieldModificationWorkAtom)other);
	}

	public final boolean equals(final FieldModificationWorkAtom other) {
		return
			getTransactable().equals(other.getTransactable()) &&
			field.equals(other.field) &&
			preValue.equals(other.preValue) &&
			postValue.equals(other.postValue);
	}
	
	public int hashCode() {
		// TODO: should hash on all values, not just on the domain object.
		return getTransactable().hashCode();
	}

	/**
	 * 
	 * @uml.property name="field"
	 */
	public Field getField() {
		return field;
	}

	/**
	 * 
	 * @uml.property name="preValue"
	 */
	public Object getPreValue() {
		return preValue;
	}

	/**
	 * 
	 * @uml.property name="postValue"
	 */
	public Object getPostValue() {
		return postValue;
	}

	
}
