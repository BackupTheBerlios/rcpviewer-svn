package de.berlios.rcpviewer.metamodel;

import java.util.Map;

/**
 * Creates adapters from information stored in a Map<String, String>.
 * 
 * <p>
 * Because we use EMF to store the data, any extension (adapters) attached to
 * a {@link de.berlios.rcpviewer.metamodel.IDomainClass} must be serialized into
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
 * Map<String, String>.  (The (@link de.berlios.rcpviewer.metamodel.IDomainClass}
 * takes care of moving the Map into the {@link org.eclipse.emf.ecore.EAnnotation}.
 * 
 * @author Dan Haywood
 */
public interface IAdapterFactory<T> {
	public T createAdapter(Map<String, String> details);
}
