package org.essentialplatform.progmodel.essential.runtime;

import org.essentialplatform.progmodel.essential.core.util.EssentialProgModelStandardSemanticsRules;
import org.essentialplatform.progmodel.standard.*;

import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;

import org.essentialplatform.core.util.MethodNameHelper;
import org.essentialplatform.runtime.domain.IDomainObject;

/**
 * Helper class (package local) that encapsulates the various naming 
 * conventions of the standard programming model.
 * 
 * @author Dan Haywood
 */
public final class EssentialProgModelRuntimeRules extends EssentialProgModelStandardSemanticsRules {

	public EssentialProgModelRuntimeRules() {}
	
	// ATTRIBUTES: START
	
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
	 * Determines name of attribute from Method name (can represent either an
	 * accessor or a mutator Method).
	 * 
	 * @param accessorOrMutator
	 * @return
	 */
	public final String deriveAttributeName(final Method accessorOrMutator) {
		return deriveAttributeName(accessorOrMutator.getName());
	}

	public boolean isIsUnsetMethodFor(Method method, EAttribute attribute) {
		if (method.getParameterTypes().length != 0) {
			return false;
		}
		Class returnType = method.getReturnType();
		if (!returnType.equals(boolean.class)) {
			return false;
		}
		String attributeName = attribute.getName();
		String isUnsetMethodName = "isUnset" + new MethodNameHelper().titleCase(attributeName);
		if (!method.getName().equals(isUnsetMethodName)) {
			return false;
		}
		return true;
	}

	public boolean isUnsetMethodFor(Method method, EAttribute attribute) {
		if (method.getParameterTypes().length != 0) {
			return false;
		}
		Class returnType = method.getReturnType();
		if (!returnType.equals(void.class)) {
			return false;
		}
		String attributeName = attribute.getName();
		String unsetMethodName = "unset" + new MethodNameHelper().titleCase(attributeName);
		if (!method.getName().equals(unsetMethodName)) {
			return false;
		}
		return true;
	}

	// ATTRIBUTES: END

	// LINKS: START
	
	/**
	 * Whether this method represents an accessor (getter) to either a simple
	 * or multiple reference of other {@link ADomainObject}s.
	 * 
	 * @param method
	 * @return
	 */
	public final boolean isReference(final Method method) {
		if (method.getParameterTypes().length != 0)
			return false;
		Class<?> returnType = method.getReturnType();
		if (!isReferenceType(returnType) &&
			!getJavaProgModelRules().isCollectionType(returnType))
			return false;
		String methodName = method.getName(); 
		return methodName.startsWith("get") &&
		       methodName.length() > 3        ;
	}
	
	public final void assertReference(final Method method) {
		if (method == null) { // TODO: make into an aspect
			throw new AssertionError("null method");
		}
		if (!isReference(method)) {
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
	
	public final boolean isAssociatorFor(Method method, EReference eReference) {
		return isAssociatorOrDissociatorFor(method, eReference, 
				eReference.isMany()?"addTo":"associate");
	}

	public final boolean isDissociatorFor(Method method, EReference eReference) {
		return isAssociatorOrDissociatorFor(method, eReference, 
				eReference.isMany()?"removeFrom":"dissociate");
	}

	private boolean isAssociatorOrDissociatorFor(Method method, EReference eReference, final String prefix) {
		if (method.getParameterTypes().length != 1) {
			return false;
		}
		Class paramType = method.getParameterTypes()[0];
		Class referencedType = eReference.getEReferenceType().getInstanceClass();
		if (!paramType.equals(referencedType)) {
			return false;
		}
		String referenceName = eReference.getName();
		String associatorMethodName = prefix + new MethodNameHelper().titleCase(referenceName);
		if (!method.getName().equals(associatorMethodName)) {
			return false;
		}
		return true;
	}


	/**
	 * Determines name of association from Method name (can represent any of a
	 * link method (getter), an associate method or a dissociate Method).
	 * 
	 * @param associationMethod
	 * @return
	 */
	public final String deriveReferenceName(final Method associationMethod) {
		return deriveReferenceName(associationMethod.getName());
	}

	// LINKS: END


	/**
	 * If method is inherited from {@link java.lang.Object}, or has been 
	 * introduced via AspectJ, then is considered reserved and so should never 
	 * be considered to be an operation.
	 * 
	 * <p>
	 * Implementation Note: we recognise AspectJ introductions simply by them
	 * being prefixed "ajc$".  (A better design might be to introduce an
	 * annotation also?)
	 * 
	 * @param method
	 * @return
	 */
	public boolean isReserved(Method method) {
		if (method.getDeclaringClass().equals(Object.class)) {
			return true;
		}
		if (method.getName().startsWith("ajc$")) {
			return true;
		}
		return false;
	}

}

