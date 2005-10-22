package de.berlios.rcpviewer.progmodel.java;

import de.berlios.rcpviewer.domain.MethodNameHelper;

/**
 * Helper class (package local) that encapsulates the various naming 
 * conventions of the java "programming model".
 * 
 * <p>
 * This class is final; there should be no need to subclass for runtime or
 * compile-time environments.
 * 
 * @author Dan Haywood
 */
public final class JavaProgModelRules {

	public JavaProgModelRules() {}
	
	// ATTRIBUTES: START
	
	/**
	 * If one of the (hard-coded) classes in 
	 * {@link StandardProgModelConstants#JDK_VALUE_TYPES}.
	 * 
	 */
	public final boolean isCoreValueType(final Class<?> javaClass) {
		return JavaProgModelConstants.JDK_VALUE_TYPES.contains(javaClass);
	}
	
	public final boolean isCollectionType(final Class<?> clazz) {
		return JavaProgModelConstants.COLLECTION_TYPES.contains(clazz);
	}

	public boolean isVoid(Class<?> javaDataType) {
		return Void.TYPE.equals(javaDataType);
	}


}

