package de.berlios.rcpviewer.domain;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * 
 * @author Dan Haywood
 */
public interface IRuntimeDomainClassAdapter<T> /* extends IDomainClassAdapter<T>*/ {
	
	/**
	 * Returns the per-object adapter for an instance of a {@link IDomainObject}
	 * corresponding to this class adapter, PROVIDED that the required 
	 * objectAdapterClass is the one that belongs to (is compatible with, 
	 * {@link #isCompatible()}) this class adapter. 
	 * 
	 * <p>
	 * Otherwise, just returns null.
	 * 
	 * @param domainObject
	 * @param class of the required object adapter.
	 * @return
	 */
	<V> V getObjectAdapterFor(IDomainObject<T> domainObject, Class<V> objectAdapterClass);
		
	/**
	 * Whether the object adapter class is compatible with this class.
	 * 
	 * @param objectAdapterClass
	 * @return
	 */
	<V> boolean isCompatible(Class<V> objectAdapterClass);
}
