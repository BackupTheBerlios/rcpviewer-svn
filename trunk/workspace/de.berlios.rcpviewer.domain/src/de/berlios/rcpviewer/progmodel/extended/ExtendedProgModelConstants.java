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
