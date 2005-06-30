package de.berlios.rcpviewer.progmodel.standard;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;

import de.berlios.rcpviewer.domain.AbstractDomainClass;
import de.berlios.rcpviewer.domain.MethodNameHelper;

/**
 * Helper class that encapsulates the various naming conventions of the
 * standard programming model.
 * 
 * @author Dan Haywood
 */
public class NamingConventions {

	// ATTRIBUTES: START
	
	/**
	 * @see deriveAttributeName
	 * @param getterOrSetter
	 * @return
	 */
	public final String deriveAttributeName(final String name) {
		if (!name.startsWith("get") && !name.startsWith("is") && !name.startsWith("set")) {
			throw new IllegalArgumentException("getter or setter expected");
		}
		if ( (name.startsWith("get") || name.startsWith("set") )) {
			if (name.length() <= 3) {
				throw new IllegalArgumentException("getter or setter name too short");
			}
			return new MethodNameHelper().camelCase(name.substring(3));
		}
		if (name.startsWith("is")) {
			if (name.length() <= 2) {
				throw new IllegalArgumentException("is getter name too short");
			}
			return new MethodNameHelper().camelCase(name.substring(2));
		}
		throw new IllegalArgumentException("getter or setter expected");
	}
	
	// ATTRIBUTES: END

	// OPERATIONS: START

	// OPERATIONS: END

	// LINKS: START
	
	/**
	 * @see deriveLinkName
	 * @param name
	 * @return
	 */
	public final String deriveLinkName(final String name) {
		if (name.startsWith("get")) {
			if (name.length() <= 3) {
				throw new IllegalArgumentException("name of link method too short");
			}
			return new MethodNameHelper().camelCase(name.substring(3));
		}
		if (name.startsWith("associate")) {
			if (name.length() <= 9) {
				throw new IllegalArgumentException("name of associator method too short");
			}
			return new MethodNameHelper().camelCase(name.substring(9));
		}
		if (name.startsWith("dissociate")) {
			if (name.length() <= 10) {
				throw new IllegalArgumentException("name of dissociator method too short");
			}
			return new MethodNameHelper().camelCase(name.substring(10));
		}
		throw new IllegalArgumentException("association method expected");
	}

	// LINKS: END


	/**
	 * If one of the (hard-coded) classes in {@link StandardProgModelConstants#JDK_VALUE_TYPES},
	 * or implements a {@link ValueObject}.
	 * 
	 * TODO: use annotations rather than interface inheritance.
	 * 
	 */
	public final boolean isValueType(final Class<?> javaClass) {
		if (de.berlios.rcpviewer.domain.TypeConstants.JDK_VALUE_TYPES.contains(javaClass)) {
			return true;
		}
		Value valueAnnotation = javaClass.getAnnotation(Value.class);
		return valueAnnotation != null;
	}
	
	/**
	 * A reference type if has the @InDomain annotation.
	 *  
	 * @param javaClass
	 * @return
	 */
	public final <V> boolean isReferenceType(final Class<V> javaClass) {
		if (javaClass == null) {
			return false;
		}
		if (javaClass == void.class) {
			return false;
		}
		InDomain domain = javaClass.getAnnotation(InDomain.class);
		return domain != null;
	}
	
	public final boolean isCollectionType(final Class<?> clazz) {
		return de.berlios.rcpviewer.domain.TypeConstants.COLLECTION_TYPES.contains(clazz);
	}

	public boolean isVoid(Class<?> javaDataType) {
		return Void.TYPE.equals(javaDataType);
	}


}

