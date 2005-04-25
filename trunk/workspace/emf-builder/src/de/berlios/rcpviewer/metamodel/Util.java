package de.berlios.rcpviewer.metamodel;


/**
 * Locally used utility methods for manipulating method names etc.
 * 
 * TODO: need some tests for this.
 */
public final class Util {
	
	/**
	 * cannot instantiate.
	 *
	 */
	private Util() {}

	/**
	 * Ensures that the first letter is lower case.  The remainder of the word
	 * is untouched.
	 * @param word
	 * @return
	 */
	public final static String camelCase(final String word) {
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
	public final static String titleCase(final String word) {
		return
		Character.toUpperCase(word.charAt(0)) +
			(word.length() > 1?word.substring(1):"");
	}

}
