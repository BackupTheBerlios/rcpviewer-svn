package org.essentialplatform.core.domain.adapters;

import java.util.Map;

import org.essentialplatform.core.domain.IDomainClass;

/**
 * Creates adapters from information stored in a Map<String, String>.
 * 
 * <p>
 * Because we use EMF to store the data, any extension (adapters) attached to
 * a {@link org.essentialplatform.core.domain.IDomainClass} must be serialized into
 * key/value pairs of Strings.  This then allows them to be stored within the
 * {@link org.eclipse.emf.ecore.EAnnotation#getDetails()} map. 
 * 
 * <p>
 * On the other hand, the consumers of {@link IDomainClass} want an object that
 * implements the extension (adapter) class already and don't really care where
 * this object gets its information from.
 * 
 * <p>
 * The job of this class then is to construct adapters for a specified type
 * (the parameterized type, in fact) but using information stored within a
 * Map<String, String>.  (The (@link org.essentialplatform.domain.IDomainClass}
 * takes care of moving the Map into the {@link org.eclipse.emf.ecore.EAnnotation}.
 * 
 * @author Dan Haywood
 */
public interface IAdapterFactory<T> {
	
	/**
	 * The details for the {@link IDomainClass} to store (using EMF).
	 * 
	 * @return
	 */
	public Map<String, String> getDetails();
	
	/**
	 * The adapter created later on.
	 * 
	 * @param details
	 * @return
	 */
	public T createAdapter(IDomainClass adaptedDomainClass);
}
