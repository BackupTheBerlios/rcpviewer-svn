package org.essentialplatform.runtime.shared.util;

public final class StringUtil {

	/**
	 * Cannot instantiate.
	 *
	 */
	private StringUtil() { }

	public static String camelCase(final String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		return Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}

}
