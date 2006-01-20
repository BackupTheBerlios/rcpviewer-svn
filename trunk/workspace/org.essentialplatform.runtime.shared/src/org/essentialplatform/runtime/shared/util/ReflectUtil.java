package org.essentialplatform.runtime.shared.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ReflectUtil {

	/**
	 * Cannot instantiate
	 *
	 */
	private ReflectUtil() { }

	
	/**
	 * Locates the field called _xxx or xxx for a method called addToXxx.
	 * 
	 * <p>
	 * No longer used since using addingTo... as pointcut; leaving in for now
	 * unless revert to using invokeAddTo...
	 */
	public static Field getFieldForAddTo(final Method addToMethod) {
		Field field = getFieldCorrespondingToMethodWithPrefix(addToMethod, "addTo", "_");
		if (field != null) { return field; }
		field = getFieldCorrespondingToMethodWithPrefix(addToMethod, "addTo", null);
		return field;
	}

	/**
	 * Locates the field called _xxx or xxx for a method called removeFromXxx.
	 * 
	 * <p>
	 * No longer used since using removingFrom... as pointcut; leaving in for now
	 * unless revert to using invokeRemoveFrom...
	 */
	public static Field getFieldForRemoveFrom(final Method removeFromMethod) {
		Field field = getFieldCorrespondingToMethodWithPrefix(removeFromMethod, "removeFrom", "_");
		if (field != null) { return field; }
		field = getFieldCorrespondingToMethodWithPrefix(removeFromMethod, "removeFrom", null);
		return field;
	}

	public static Field getFieldCorrespondingToMethodWithPrefix(final Method method, final String methodPrefix, final String fieldPrefix) {
		if (method == null) { return null; }
		final String methodName = method.getName();
		if (!methodName.startsWith(methodPrefix)) { return null; }
		String methodNameNoPrefix = camelCase(methodName.substring(methodPrefix.length()));
		String fieldName = (fieldPrefix != null?fieldPrefix:"") + methodNameNoPrefix;
		Class<?> javaClass = method.getDeclaringClass();
		Field field;
		try {
			field = javaClass.getDeclaredField(fieldName);
		} catch (SecurityException ex) {
//			getLogger().warn("could not locate field from " + methodPrefix + " method '" + method + "' (SecurityException)", ex);
			return null;
		} catch (NoSuchFieldException ex) {
//			getLogger().warn("could not locate field from " + methodPrefix + " method '" + method + "' (NoSuchFieldException)", ex);
			return null;
		}
		return isCollectionType(field);
	}
	
	public static String camelCase(String str) {
		if (str == null) { return str; }
		switch (str.length()) {
		case 0:
			return str;
		case 1:
			str = Character.toLowerCase(str.charAt(0)) + "";
			break;
		default:
			str = Character.toLowerCase(str.charAt(0)) + str.substring(1);
			break;
		}
		return str;
	}

	/**
	 * If the field is has a declared type of Collection (or any subclass) then
	 * return it, otherwise return null.
	 */
	public static Field isCollectionType(final Field field) {
		Class<?> fieldReturnType = field.getType();
		if (java.util.Collection.class.isAssignableFrom(fieldReturnType)) {
			return field;
		}
		return null;
	}


}
