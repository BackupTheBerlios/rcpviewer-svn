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
	 * Presence of an EAnnotation with this source on an EClass indicates
	 * additional information accessible from the details.
	 */
	public static final String ANNOTATION_CLASS = 
		"http://rcpviewer.berlios.de/progmodel/extended/class";

	/**
	 * Presence of an EAnnotation with this source on an EAttribute indicates
	 * additional information accessible from the details.
	 */
	public static final String ANNOTATION_ATTRIBUTE = 
		"http://rcpviewer.berlios.de/progmodel/extended/attribute";

	/**
	 * If present in the details of an EAnnotation with source of
	 * {@link #ANNOTATION_ATTRIBUTE}, then indicates the relative position of
	 * this attribute with respect to the other attributes.
	 * 
	 * <p>
	 * The value that is held (as a string) can be parsed into an integer.
	 */
	public static final String ANNOTATION_ATTRIBUTE_POSITIONED_AT_KEY = 
		"positionedAt";


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
	public static final String ANNOTATION_OPERATION_POSITIONED_AT_KEY = 
		"positionedAt";

	/**
	 * Key to details of EAnnotation with source of {@link ExtendedProgModelConstants#ANNOTATION_ELEMENT}
	 * whose presence indicates that the annotated type should NOT be 
	 * explicitly instantiatable. 
	 */
	public static final String ANNOTATION_ELEMENT_INSTANTIABLE_KEY = 
		"instantiable";

	/**
	 * Key to details of EAnnotation with source of {@link ExtendedProgModelConstants#ANNOTATION_ELEMENT}
	 * whose presence indicates that the annotated type should NOT be 
	 * explicitly saveable. 
	 */
	public static final String ANNOTATION_ELEMENT_SAVEABLE_KEY = 
		"saveable";

	/**
	 * Key to details of EAnnotation with source of {@link ExtendedProgModelConstants#ANNOTATION_ELEMENT}
	 * whose presence indicates that the annotated type should be made 
	 * available in a generic search capability.
	 */
	public static final String ANNOTATION_ELEMENT_SEARCHABLE_KEY = 
		"searchable";

}
