package de.berlios.rcpviewer.progmodel.standard;

public final class Constants {

	private Constants() {
	}


	/**
	 * Key to EAnnotation details representing (names of) methods to access or
	 * otherwise modify the value of an attribute.
	 * 
	 * <p>
	 * Holds key to the accessor method (applicable only if not write only).
	 */
	public static final String ANNOTATION_ATTRIBUTE_ACCESSOR_METHOD_NAME_KEY = "get";

	/**
	 * Key to EAnnotation details representing (names of) methods to access or
	 * otherwise modify the value of an attribute.
	 * 
	 * <p>
	 * Holds key to the mutator method (applicable only if changeable).
	 */
	public static final String ANNOTATION_ATTRIBUTE_UNSET_METHOD_NAME_KEY = "set";

	/**
	 * Key to EAnnotation details representing (names of) methods to access or
	 * otherwise modify the value of an attribute.
	 * 
	 * <p>
	 * Holds key to the isUnset method (applicable only if isUnsettable is true).
	 */
	public static final String ANNOTATION_ATTRIBUTE_IS_UNSET_METHOD_NAME_KEY = "isUnset";

	/**
	 * Key to EAnnotation details representing (names of) methods to access or
	 * otherwise modify the value of an attribute.
	 * 
	 * <p>
	 * Holds key to the unset method (applicable only if isUnsettable is true).
	 */
	public static final String ANNOTATION_ATTRIBUTE_MUTATOR_METHOD_NAME_KEY = "unset";

	public static final String ANNOTATION_CLASS_NAME = "className";


}
