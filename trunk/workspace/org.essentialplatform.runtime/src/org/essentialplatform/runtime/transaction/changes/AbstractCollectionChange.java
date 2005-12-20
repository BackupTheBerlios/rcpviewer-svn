package org.essentialplatform.runtime.transaction.changes;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Collections;
import java.util.Set;
import java.util.List;

import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IDomainObject.IObjectCollectionReference;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;


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
public abstract class AbstractCollectionChange<V> extends AbstractChange implements IModificationChange {

	protected static Object[] extendedInfo(final ITransactable transactable, final Object referencedObject) {
		String referencedObj = "obj: '" + referencedObject; 
		return new Object[]{referencedObj};
	}
	
	/**
	 * This change's reference to the collection object being changed.
	 */
	private transient Collection<V> _collection;
	
	/**
	 * The value of the {@link #getField()} after it was modified, accessed
	 * through {@link #getReferencedObject()}.
	 */
	protected final V _referencedObject;

	/**
	 * <tt>transient</tt> for serialization.
	 */
	protected transient IObjectCollectionReference _reference;


	private static String nameOf(IDomainObject.IObjectCollectionReference reference) {
		return reference != null? reference.getReference().getName(): "???";
	}
	
	/**
	 * 
	 * @param attribute
	 * @param referencedObject
	 */
	public AbstractCollectionChange(
			final ITransaction transaction,
			final ITransactable transactable,
			final Collection<V> collection,
			final V referencedObject, 
			final IDomainObject.IObjectCollectionReference reference) {
		super(transaction, transactable, nameOf(reference), extendedInfo(transactable, referencedObject), false);
		_collection = collection;
		if (collection == null) {
			throw new RuntimeException("No collection!");
		}
		if (referencedObject instanceof ITransactable) {
			modifies((ITransactable)referencedObject);
		}
		_referencedObject = referencedObject;
		_reference = reference;
	}

	/**
	 * For testing of comparators only 
	 */
	protected AbstractCollectionChange() {
		_referencedObject = null;
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
	public V getReferencedObject() {
		return _referencedObject;
	}

}
