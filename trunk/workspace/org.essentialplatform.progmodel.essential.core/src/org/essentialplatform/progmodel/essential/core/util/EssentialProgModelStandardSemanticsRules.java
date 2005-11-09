package org.essentialplatform.progmodel.essential.core.util;

import org.essentialplatform.core.util.MethodNameHelper;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Value;

/**
 * Helper class (package local) that encapsulates the various naming 
 * conventions of the standard programming model.
 * 
 * <p>
 * This class is abstract so that it can be used in both the runtime and
 * compile-time environments.
 * 
 * @author Dan Haywood
 */
public abstract class EssentialProgModelStandardSemanticsRules {

	public EssentialProgModelStandardSemanticsRules() {}
	
	private JavaRules _javaProgModelRules = new JavaRules();
	
	protected JavaRules getJavaProgModelRules() {
		return _javaProgModelRules;
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
	

	
	/**
	 * @param name
	 * @return
	 */
	public final String deriveReferenceName(final String name) {
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



	/**
	 * If a core value type, or is annotated as a Value.
	 * 
	 */
	public final boolean isValueType(final Class<?> javaClass) {
		if (_javaProgModelRules.isCoreValueType(javaClass)) {
			return true;
		}
		Value valueAnnotation = javaClass.getAnnotation(Value.class);
		return valueAnnotation != null;
	}
	
	/**
	 * Returns the name of the domain to which a non-core value type belongs.
	 * 
	 * <p>
	 * If the supplied class does not repreesnt a non-core value type then an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param javaClass
	 * @return
	 */
	public final String getValueTypeDomainName(final Class<?> javaClass) {
		if (!isValueType(javaClass)) {
			throw new IllegalArgumentException("Class must represent a value type");
		}
		if (_javaProgModelRules.isCoreValueType(javaClass)) {
			throw new IllegalArgumentException("Class must not be a core value type");
		}
		// should have a Value annotation.
		Value value = javaClass.getAnnotation(Value.class);
		if (value == null) {
			// shouldn't happen.
			throw new IllegalArgumentException("Class is non-core value and so should have an @Value annotation!");
		}
		return value.value();
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
}

