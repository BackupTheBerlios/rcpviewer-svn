package org.essentialplatform.progmodel.essential.core.util;

import org.essentialplatform.core.util.MethodNameHelper;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelStandardSemanticsConstants;
import org.essentialplatform.progmodel.essential.core.JavaConstants;

/**
 * Helper class (package local) that encapsulates the various naming 
 * conventions and rules of the Java programming language; for example the 
 * definition of the primitive data types.
 * 
 * <p>
 * This class is final; there should be no need to subclass for runtime or
 * compile-time environments.
 * 
 * @author Dan Haywood
 */
public final class JavaRules {


	public JavaRules() {}
	
	// ATTRIBUTES: START
	
	/**
	 * If one of the (hard-coded) classes in 
	 * {@link EssentialProgModelStandardSemanticsConstants#JDK_VALUE_TYPES}.
	 * 
	 */
	public final boolean isCoreValueType(final Class<?> javaClass) {
		return JavaConstants.JDK_VALUE_TYPES.contains(javaClass);
	}
	
	public final boolean isCollectionType(final Class<?> clazz) {
		return JavaConstants.COLLECTION_TYPES.contains(clazz);
	}

	public boolean isVoid(Class<?> javaDataType) {
		return Void.TYPE.equals(javaDataType);
	}


}

