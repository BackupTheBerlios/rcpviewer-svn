package de.berlios.rcpviewer.transaction.internal;

import java.lang.reflect.Field;
import java.util.Collection;
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
public abstract class AbstractCollectionChange<V> extends AbstractChange {

	protected static String description(final Field field) {
		return field.getName();
	}
	
	protected static Object[] extendedInfo(final ITransactable transactable, final Field field, final Object referencedObject) {
		field.setAccessible(true);
		String referencedObj = "ref: '" + referencedObject; 
		return new Object[]{referencedObj};
	}
	/**
	 * The attribute that is being modified, accessed through 
	 * {@link #getAttribute()}.
	 */
	private final Field _field;
	
	/**
	 * This change's reference to the collection object being changed (that is,
	 * the result of <code>_field.get(_transactable)</code>).
	 */
	private final Collection<V> _collection;
	
	/**
	 * The value of the {@link #getField()} after it was modified, accessed
	 * through {@link #getReferencedObject()}.
	 */
	private final Object _referencedObject;

	/**
	 * Protected for implementation of equals etc in subclasses.
	 */
	protected final ITransactable _transactable;
	private final Set<ITransactable> _transactableAsSet;
	
	/**
	 * 
	 * @param attribute
	 * @param referencedObject
	 */
	public AbstractCollectionChange(
			final ITransactable transactable,
			final Field field,
			final V referencedObject) {
		super(description(field), extendedInfo(transactable, field, referencedObject), false);
		this._field = field;
		_field.setAccessible(true);
		_transactable = transactable;
		try {
			_collection = (Collection<V>)_field.get(_transactable);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException("Unable to obtain collection object from field", ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException("Unable to obtain collection object from field", ex);
		}
		if (_collection == null) {
			throw new RuntimeException("No collection!  field.get(Object) returns null");
		}
		Set<ITransactable> transactableAsSet = new HashSet<ITransactable>();
		transactableAsSet.add(transactable);
		_transactableAsSet = Collections.unmodifiableSet(transactableAsSet);
		_referencedObject = referencedObject;
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
	 * The collection of this particular pojo that is referenced by the field. 
	 * @return
	 */
	public Collection<V> getCollection() {
		return _collection;
	}
	
	/**
	 * The object being added or removed from the collection (which depends on
	 * the subtype).
	 * 
	 * @return
	 */
	public Object getReferencedObject() {
		return _referencedObject;
	}

}
