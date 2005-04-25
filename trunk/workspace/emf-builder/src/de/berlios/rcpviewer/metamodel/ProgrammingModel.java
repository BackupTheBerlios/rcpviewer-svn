package de.berlios.rcpviewer.metamodel;

import java.lang.reflect.Method;
import de.berlios.rcpviewer.metamodel.annotations.*;

/**
 * Concrete implementation of {@link IProgrammingModel} for interpreting
 * a POJO model (with rcpviewer annotations).
 *
 */
public final class ProgrammingModel implements IProgrammingModel {

	/**
	 * conforms to the signature of a getter of a value.  References to
	 * {@link DomainClass}es are treated as {@link Link}s.
	 * @param method
	 * @return
	 */
	public final boolean isAccessor(final Method method) {
		if (method.getParameterTypes().length != 0)
			return false;
		Class returnType = method.getReturnType();
		if (!isValueType(returnType))
			return false;
		String methodName = method.getName(); 
		return ( methodName.startsWith("get") &&
		         methodName.length() > 3       ) ||
			   ( methodName.startsWith("is") &&
			     methodName.length() > 2     &&
			    (returnType.equals(Boolean.class) ||
			     returnType.equals(boolean.class)   ) );
	}

	public final boolean isMutator(final Method method) {
		Class[] parameterTypes = method.getParameterTypes();
		return 
		   method.getReturnType() == void.class &&
		   parameterTypes.length == 1 &&
		   isValueType(parameterTypes[0]) &&
		   method.getName().startsWith("set") &&
		   method.getName().length() > 3;
	}

	/**
	 * assures that the supplied {@link Method} conforms to the expectations of
	 * being a getter.
	 * 
	 * @throws AssertionError otherwise
	 * @param accessor
	 */
	public final void assertAccessor(final Method accessor) {
		if (accessor == null) {
			throw new AssertionError("null accessor");
		}
		if (!isAccessor(accessor)) {
			throw new AssertionError("not an accessor");
		}
	}

	/**
	 * assures that the supplied Method conforms to the expectations of
	 * being a setter.
	 * @throws AssertionError otherwise
	 * @param dissociator
	 */
	public final void assertMutator(final Method setter) {
		if (setter == null) {
			throw new AssertionError("null setter");
		}
		if (!isMutator(setter)) {
			throw new AssertionError("not a setter");
		}
	}

	/**
	 * The type of the getter (its return type).
	 * @param accessor
	 * @return
	 */
	public final Class accessorType(final Method accessor) {
		assertAccessor(accessor);
		return accessor.getReturnType();
	}

	/**
	 * The type of the setter (the type of its first argument).
	 * @param mutator
	 * @return
	 */
	public final Class mutatorType(final Method mutator) {
		assertMutator(mutator);
		return mutator.getParameterTypes()[0];
	}

	/**
	 * indicates whether supplied getter and setter are compatible, that is, that they
	 * have the same type and the same name. 
	 * @param getter
	 * @param setter
	 * @return
	 */
	public final boolean isCompatible(final Method getter, final Method setter) {
		assertAccessor(getter);
		assertMutator(setter);
		if (!accessorType(getter).equals(mutatorType(setter))) {
			return false;
		}
		if (!deriveAttributeName(getter).equals(deriveAttributeName(setter))) {
			return false;
		}
		return true;
	}

	/**
	 * Determines name of attribute from Method name (can represent either an
	 * accessor or a mutator Method).
	 * 
	 * @param accessorOrMutator
	 * @return
	 */
	public final String deriveAttributeName(final Method accessorOrMutator) {
		return deriveAttributeName(accessorOrMutator.getName());
	}

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
			return Util.camelCase(name.substring(3));
		}
		if (name.startsWith("is")) {
			if (name.length() <= 2) {
				throw new IllegalArgumentException("is getter name too short");
			}
			return Util.camelCase(name.substring(2));
		}
		throw new IllegalArgumentException("getter or setter expected");
	}
	

	/**
	 * If one of the (hard-coded) classes in {@link Constants#JDK_VALUE_TYPES},
	 * or implements a {@link ValueObject}.
	 * 
	 * TODO: use annotations rather than interface inheritance.
	 * 
	 */
	public final boolean isValueType(final Class<?> clazz) {
		return Constants.JDK_VALUE_TYPES.contains(clazz) ||
			   ValueMarker.class.isAssignableFrom(clazz);
	}
	
	public final boolean isReferenceType(final Class<?> clazz) {
		return clazz != null &&
		       clazz != void.class &&
			   Constants.SIMPLE_REF_TYPE.isAssignableFrom(clazz);
	}
	
	public final boolean isCollectionType(final Class<?> clazz) {
		return Constants.COLLECTION_TYPES.contains(clazz);
	}
	
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Whether this method represents an accessor (getter) to either a simple
	 * or multiple reference of other {@link ADomainObject}s.
	 * 
	 * @param linkMethod
	 * @return
	 */
	public final boolean isLink(final Method linkMethod) {
		if (linkMethod.getParameterTypes().length != 0)
			return false;
		Class<?> returnType = linkMethod.getReturnType();
		if (!isReferenceType(returnType) &&
			!isCollectionType(returnType))
			return false;
		String methodName = linkMethod.getName(); 
		return methodName.startsWith("get") &&
		       methodName.length() > 3        ;
	}
	
	public final void assertLink(final Method linkMethod) {
		if (linkMethod == null) {
			throw new AssertionError("null linkMethod");
		}
		if (!isLink(linkMethod)) {
			throw new AssertionError("not a Method that represents a link");
		}
	}

	
	/**
	 * A method which takes a single {@link ADomainObject} and returns void,
	 * and whose prefix is <code>associate</code>. 
	 * @param method
	 * @return whether this method represents an associator
	 */
	public final boolean isAssociator(final Method method) {
		if (!isAssociatorOrDissociator(method))
			return false;
		String methodName = method.getName(); 
		return methodName.startsWith("associate") &&
		       methodName.length() > 9;
	}

	/**
	 * A method which takes a single {@link ADomainObject} and returns void,
	 * and whose prefix is <code>dissociate</code>. 
	 * @param method
	 * @return whether this method represents an dissociator
	 */
	public final boolean isDissociator(final Method method) {
		if (!isAssociatorOrDissociator(method))
			return false;
		String methodName = method.getName(); 
		return methodName.startsWith("dissociate") &&
		       methodName.length() > 10;
	}

	private final boolean isAssociatorOrDissociator(Method method) {
		if (method.getParameterTypes().length != 1)
			return false;
		if (method.getReturnType() != void.class)
			return false;
		Class parameterType = method.getParameterTypes()[0];
		if (!IDomainObject.class.isAssignableFrom(parameterType))
			return false;
		return true;
	}

	public final void assertAssociator(final Method associator) {
		if (associator == null) {
			throw new AssertionError("null associator method");
		}
		if (!isAssociator(associator)) {
			throw new AssertionError("not an associator");
		}
	}

	public final void assertDissociator(final Method dissociator) {
		if (dissociator == null) {
			throw new AssertionError("null dissociator method");
		}
		if (!isDissociator(dissociator)) {
			throw new AssertionError("not an dissociator");
		}
	}

	/**
	 * @param associator or dissociator method
	 * @return The type of the associator or dissociator (the type of its first argument).
	 */
	public final Class linkType(final Method linkMethod) {
		if (linkMethod == null) {
			throw new AssertionError("null associator/dissociator method");
		}
		if (!isAssociator(linkMethod) &&
			!isDissociator(linkMethod)  ) {
			throw new AssertionError("not an associator/dissociator method");
		}
		return linkMethod.getParameterTypes()[0];
	}

	/**
	 * Determines name of association from Method name (can represent any of a
	 * link method (getter), an associate method or a dissociate Method).
	 * 
	 * @param associationMethod
	 * @return
	 */
	public final String deriveLinkName(final Method associationMethod) {
		return deriveLinkName(associationMethod.getName());
	}

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
			return Util.camelCase(name.substring(3));
		}
		if (name.startsWith("associate")) {
			if (name.length() <= 9) {
				throw new IllegalArgumentException("name of associator method too short");
			}
			return Util.camelCase(name.substring(9));
		}
		if (name.startsWith("dissociate")) {
			if (name.length() <= 10) {
				throw new IllegalArgumentException("name of dissociator method too short");
			}
			return Util.camelCase(name.substring(10));
		}
		throw new IllegalArgumentException("association method expected");
	}

	/**
	 * indicates whether supplied associator and dissociator are compatible,
	 * that is, that they have the same type and the same name. 
	 * @param associator
	 * @param dissociator
	 * @return
	 */
	public final boolean isLinkPairCompatible(final Method associator, final Method dissociator) {
		assertAssociator(associator);
		assertDissociator(dissociator);
		if (!linkType(associator).equals(linkType(dissociator))) {
			return false;
		}
		if (!deriveLinkName(associator).equals(deriveLinkName(dissociator))) {
			return false;
		}
		return true;
	}

	public boolean representsAttribute(Method method) {
		return (isAccessor(method) || isMutator(method)) &&
				isValueType(method.getReturnType());
	}
	
}

