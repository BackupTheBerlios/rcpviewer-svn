package de.berlios.rcpviewer.progmodel.extended;


/**
 * Set of constants relating to this package.
 * 
 * @author Dan Haywood
 */
public final class ExtendedProgModelConstants {

	
	/**
	 * Cannot instantiate.
	 */
	private ExtendedProgModelConstants() {
	}

	public final static int FIELD_LENGTH_OF_DEFAULT = 32;
	public final static int MAX_LENGTH_OF_DEFAULT = 64;
	public final static int MIN_LENGTH_OF_DEFAULT = 0;
	
	/**
	 * Presence of an EAnnotation with this source on any EModelElement 
	 * indicates additional information accessible from the details.
	 */
	public static final String ANNOTATION_ELEMENT = 
		"http://rcpviewer.berlios.de/progmodel/extended/element";

	/**
	 * Presence of an EAnnotation with this source on an EClass indicates
	 * additional information accessible from the details.
	 */
	public static final String ANNOTATION_CLASS = 
		"http://rcpviewer.berlios.de/progmodel/extended/class";

	/**
	 * Key to details of EAnnotation with source of 
	 * {@link ExtendedProgModelConstants#ANNOTATION_CLASS}
	 * whose presence indicates whether the annotated class should be 
	 * explicitly instantiatable through the UI. 
	 */
	public static final String ANNOTATION_CLASS_INSTANTIABLE_KEY = 
		"instantiable";

	/**
	 * Key to details of EAnnotation with source of 
	 * {@link ExtendedProgModelConstants#ANNOTATION_CLASS} whose presence 
	 * indicates whether the annotated class should be explicitly saveable. 
	 */
	public static final String ANNOTATION_CLASS_SAVEABLE_KEY = 
		"saveable";

	/**
	 * Key to details of EAnnotation with source of 
	 * {@link ExtendedProgModelConstants#ANNOTATION_CLASS} whose presence 
	 * indicates that the annotated class should be made available in a generic 
	 * search capability.
	 */
	public static final String ANNOTATION_CLASS_SEARCHABLE_KEY = 
		"searchable";

	/**
	 * Presence of an EAnnotation with this source on an EAttribute indicates
	 * additional information accessible from the details.
	 */
	public static final String ANNOTATION_ATTRIBUTE = 
		"http://rcpviewer.berlios.de/progmodel/extended/attribute";


	/**
	 * The suffix for a method name that represents the preconditions of
	 * a read or read/write attribute.
	 * 
	 * <p>
	 * For example, an attribute <i>firstName</i> would correspond to
	 * <i>getFirstName<b>Pre</b></i> (where <i>Pre</i> is the value of
	 * this constant).
	 * 
	 */
	public static final String PRECONDITIONS_ATTRIBUTE_SUFFIX = "Pre";

	/**
	 * Key to EAnnotation details representing (names of) method representing
	 * preconditions of an attribute.
	 * 
	 * <p>
	 * Holds key to the accessor method (applicable only if not write only).
	 */
	public static final String ANNOTATION_ATTRIBUTE_PRECONDITION_METHOD_NAME_KEY = "PRE";

	/**
	 * If present in the details of an EAnnotation with source of
	 * {@link #ANNOTATION_ATTRIBUTE}, then indicates the relative position of
	 * this attribute with respect to the other attributes.
	 * 
	 * <p>
	 * The value that is held (as a string) can be parsed into an integer.
	 */
	public static final String ANNOTATION_ATTRIBUTE_ORDER_KEY = 
		"order";

	/**
	 * If present in the details of an EAnnotation with source of
	 * {@link #ANNOTATION_ATTRIBUTE}, then indicates whether this attribute is
	 * optional.
	 * 
	 * <p>
	 * The string <i>"true"</i> is held as the value.
	 */
	public static final String ANNOTATION_ATTRIBUTE_OPTIONAL_KEY = 
		"optional";

	/**
	 * If present in the details of an EAnnotation with source of
	 * {@link #ANNOTATION_ATTRIBUTE}, then indicates whether this attribute is
	 * invisible.
	 * 
	 * <p>
	 * The string <i>"true"</i> is held as the value.
	 */
	public static final String ANNOTATION_ATTRIBUTE_INVISIBLE_KEY = 
		"invisible";

	/**
	 * If present in the details of an EAnnotation with source of
	 * {@link #ANNOTATION_ATTRIBUTE}, then indicates that this attribute is
	 * part of a business key.
	 * 
	 * <p>
	 * The value is the name of the business key.
	 * 
	 * <p>
	 * There will also be a key 
	 * ({@link #ANNOTATION_ATTRIBUTE_BUSINESS_KEY_POS_KEY}) in the details that 
	 * represents the position of this attribute in the business key. 
	 */
	public static final String ANNOTATION_ATTRIBUTE_BUSINESS_KEY_NAME_KEY = 
		"businessKey#name";

	/**
	 * If present in the details of an EAnnotation with source of
	 * {@link #ANNOTATION_ATTRIBUTE}, then indicates that this attribute is
	 * part of a business key.
	 * 
	 * <p>
	 * The value is the numeric position of the attribute in the business key, 
	 * as a string (use {@link Integer#parseInt(java.lang.String)} to obtain).
	 * 
	 * <p>
	 * There will also be a key 
	 * ({@link #ANNOTATION_ATTRIBUTE_BUSINESS_KEY_NAME_KEY}) in the details that 
	 * represents the actual name of the business key in which this attribute 
	 * is a part.
	 * 
	 */
	public static final String ANNOTATION_ATTRIBUTE_BUSINESS_KEY_POS_KEY = 
		"businessKey#pos";

	/**
	 * If present in the details of an EAnnotation with source of
	 * {@link #ANNOTATION_ATTRIBUTE}, then indicates the minimum length
	 * of this (string) attribute.
	 * 
	 * <p>
	 * The value held is the length, as a string (use 
	 * {@link Integer#parseInt(java.lang.String)} to obtain).
	 * 
	 */
	public static final String ANNOTATION_ATTRIBUTE_MIN_LENGTH_OF_KEY = 
		"minLengthOf";

	/**
	 * If present in the details of an EAnnotation with source of
	 * {@link #ANNOTATION_ATTRIBUTE}, then indicates the maximum length
	 * of this (string) attribute.
	 * 
	 * <p>
	 * The value held is the length, as a string (use 
	 * {@link Integer#parseInt(java.lang.String)} to obtain).
	 * 
	 */
	public static final String ANNOTATION_ATTRIBUTE_MAX_LENGTH_OF_KEY = 
		"maxLengthOf";

	/**
	 * If present in the details of an EAnnotation with source of
	 * {@link #ANNOTATION_ATTRIBUTE}, then indicates the field length
	 * of this (string) attribute.
	 * 
	 * <p>
	 * The value held is the length, as a string (use 
	 * {@link Integer#parseInt(java.lang.String)} to obtain).
	 * 
	 */
	public static final String ANNOTATION_ATTRIBUTE_FIELD_LENGTH_OF_KEY = 
		"fieldLengthOf";

	/**
	 * If present in the details of an EAnnotation with source of
	 * {@link #ANNOTATION_ATTRIBUTE}, then indicates a mask to validate the
	 * contents of this (string) attribute against.
	 * 
	 * <p>
	 * The value held is the mask literal. 
	 * 
	 */
	public static final String ANNOTATION_ATTRIBUTE_MASK_KEY = 
		"mask";

	/**
	 * If present in the details of an EAnnotation with source of
	 * {@link #ANNOTATION_ATTRIBUTE}, then indicates a regex to validate the
	 * contents of this (string) attribute against.
	 * 
	 * <p>
	 * The value held is the regex literal. 
	 * 
	 */
	public static final String ANNOTATION_ATTRIBUTE_REGEX_KEY = 
		"regex";

	/**
	 * If present in the details of an EAnnotation with source of
	 * {@link #ANNOTATION_ATTRIBUTE}, then indicates whether this attribute 
	 * should be considered as immutable once its owning object has been
	 * persisted.
	 * 
	 * <p>
	 * The string <i>"true"</i> is held as the value.
	 */
	public static final String ANNOTATION_ATTRIBUTE_IMMUTABLE_ONCE_PERSISTED_KEY = 
		"immutableOncePersistsed";


	/**
	 * Presence of an EAnnotation with this source on an EOperation indicates
	 * additional information accessible from the details.
	 */
	public static final String ANNOTATION_OPERATION = 
		"http://rcpviewer.berlios.de/progmodel/extended/operation";

	/**
	 * If present in the details of an EOperation with source of
	 * {@link #ANNOTATION_OPERATION}, then indicates the relative position of
	 * this operation with respect to the other operations.
	 * 
	 * <p>
	 * The value that is held (as a string) can be parsed into an integer.
	 */
	public static final String ANNOTATION_OPERATION_ORDER_KEY = 
		"order";

	/**
	 * Presence of an EAnnotation with this source on an EParameter indicates
	 * additional information accessible from the details.
	 */
	public static final String ANNOTATION_OPERATION_PARAMETER = 
		"http://rcpviewer.berlios.de/progmodel/extended/operation/parameter";


	/**
	 * If present in the details of an EParameter with source of
	 * {@link #ANNOTATION_OPERATION_PARAMETER} then indicates that the
	 * parameter is optional.
	 * 
	 * <p>
	 * The value held is always the string <i>"true"</i>.
	 */
	public static final String ANNOTATION_OPERATION_PARAMETER_OPTIONAL_KEY = null;

	/**
	 * Key to details of EAnnotation with source of {@link #ANNOTATION_ELEMENT}
	 * whose value is the description of the annotated model element.
	 */
	public static final String ANNOTATION_ELEMENT_DESCRIPTION_KEY = 
		"description";



}
