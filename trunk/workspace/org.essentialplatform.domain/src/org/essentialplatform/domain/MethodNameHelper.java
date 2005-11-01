package org.essentialplatform.domain;


/**
 * Locally used utility methods for manipulating method names etc.
 *
 * <p>
 * Clients should just instantiate and use; this class contains no state.
 * 
 * <p>
 * TODO: need some tests for this.
 * 
 * @author Dan Haywood
 */
public final class MethodNameHelper {

	/**
	 * Ensures that the first letter is lower case.  The remainder of the word
	 * is untouched.
	 * @param word
	 * @return
	 */
	public final String camelCase(final String word) {
		return
			Character.toLowerCase(word.charAt(0)) +
				(word.length() > 1?word.substring(1):"");
	}
	
	/**
	 * Ensures that the first letter is upper case.  The remainder of the word
	 * is untouched.
	 * @param word
	 * @return
	 */
	public final String titleCase(final String word) {
		return
		Character.toUpperCase(word.charAt(0)) +
			(word.length() > 1?word.substring(1):"");
	}

}
