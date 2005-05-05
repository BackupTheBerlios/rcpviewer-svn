package de.berlios.rcpviewer.progmodel.standard;

public final class Constants {

	private Constants() {
	}

	/**
	 * Access to any annotations pertaining to the {@link IDomainClass}/EClass.
	 */
	public static final String ANNOTATION_SOURCE_CLASS = 
		"http://rcpviewer.berlios.de/progmodel/standard/class";

	/**
	 * Access to the (names of the) accessor/mutator/isUnset/unset methods for
	 * an attribute, or equivalent methods for an operation.
	 */
	public static final String ANNOTATION_SOURCE_METHOD_NAMES = 
		"http://rcpviewer.berlios.de/progmodel/standard/methodNames";


	/**
	 * Presence of an EAnnotation with this source on an EAttribute indicates 
	 * that the EAttribute is write-only (has a mutator, no accessor).
	 */
	public final static String ANNOTATION_ATTRIBUTE_WRITE_ONLY = 
		"http://rcpviewer.berlios.de/progmodel/standard/attribute/writeOnly";

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

	/**
	 * Key to EAnnotation details representing (name of) underlying method that
	 * represents an operation.
	 */
	public static final String ANNOTATION_OPERATION_METHOD_NAME_KEY = "invoke";

	/**
	 * Presence of an EAnnotation with this source on an EOperation indicates 
	 * that the EOperation is static
	 */
	public static final String ANNOTATION_OPERATION_STATIC = 
		"http://rcpviewer.berlios.de/progmodel/standard/operation/static";

}
