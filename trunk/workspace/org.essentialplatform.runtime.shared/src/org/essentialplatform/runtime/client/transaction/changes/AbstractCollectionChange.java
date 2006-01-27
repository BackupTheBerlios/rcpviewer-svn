package org.essentialplatform.runtime.client.transaction.changes;

import java.util.Collection;

import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectCollectionReference;


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

	protected static Object[] extendedInfo(final IPojo transactable, final Object referencedObject) {
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
			final IPojo transactable,
			final Collection<V> collection,
			final V referencedObject, 
			final IDomainObject.IObjectCollectionReference reference) {
		super(transaction, transactable, nameOf(reference), extendedInfo(transactable, referencedObject), false);
		_collection = collection;
		if (collection == null) {
			throw new RuntimeException("No collection!");
		}
		if (referencedObject instanceof IPojo) {
			modifies((IPojo)referencedObject);
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
