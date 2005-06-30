package de.berlios.rcpviewer.progmodel.standard;

/**
 * Set of constants relating to this package.
 * 
 * <p>
 * TODO: some of these constants are runtime- or compiletime- specific.
 * 
 * @author Dan Haywood
 */
public final class StandardProgModelConstants {

	private StandardProgModelConstants() {
	}

	/**
	 * Presence of an EAnnotation with this source on an EModelElement indicates
	 * additional information accessible from the details.
	 */
	public static final String ANNOTATION_ELEMENT = 
		"http://rcpviewer.berlios.de/progmodel/standard/element";

	/**
	 * Key to details of EAnnotation with source of {@link #ANNOTATION_ELEMENT}
	 * whose value is the description of the annotated model element.
	 */
	public static final String ANNOTATION_ELEMENT_DESCRIPTION_KEY = 
		"description";

	/**
	 * Key to details of EAnnotation with source of {@link #ANNOTATION_ELEMENT}
	 * whose presence indicates that the annotated feature is immutable.
	 * 
	 * <p>
	 * Some EMF model elements, such as EAttribute, have isChangeable() 
	 * supported directly; this key is only used for EMF model elements where
	 * this is missing, eg EClass.  
	 */
	public static final String ANNOTATION_ELEMENT_IMMUTABLE_KEY = 
		"immutable";

	/**
	 * Access to any annotations pertaining (and specific to) the 
	 * {@link IDomainClass}/EClass.
	 */
	public static final String ANNOTATION_CLASS = 
		"http://rcpviewer.berlios.de/progmodel/standard/class";

	/**
	 * Presence of an EAnnotation with this source on an EAttribute indicates 
	 * that the EAttribute is write-only (has a mutator, no accessor).
	 */
	public final static String ANNOTATION_ATTRIBUTE_WRITE_ONLY = 
		"http://rcpviewer.berlios.de/progmodel/standard/attribute/writeOnly";

	/**
	 * Presence of an EAnnotation with this source on an EOperation indicates 
	 * that the EOperation is static
	 */
	public static final String ANNOTATION_OPERATION_STATIC = 
		"http://rcpviewer.berlios.de/progmodel/standard/operation/static";

	/**
	 * Presence of an EAnnotation with this source on an EReference indicates 
	 * that the reference has indicated it has an opposite.
	 */
	public static final String ANNOTATION_REFERENCE_OPPOSITE = 
		"http://rcpviewer.berlios.de/progmodel/standard/reference/opposite";

	/**
	 * Key to EAnnotation details representing (names of) opposite reference
	 * to this reference.
	 * 
	 * <p>
	 * The presence of this annotation detail does not necessarily imply that
	 * the reference does have an opposite, only that an annotation was found
	 * in the domain model (pojo) indicating that the reference should have
	 * an opposite.  Since there is no type checking, there could of course
	 * be a typo.  
	 * 
	 * <p>
	 * Holds key to the accessor method (applicable only if not write only).
	 */
	public static final String ANNOTATION_REFERENCE_OPPOSITE_KEY = "get";


	/**
	 * Access to the (names of the) accessor/mutator/isUnset/unset methods for
	 * an attribute, or an operation, or a reference.
	 */
	public static final String ANNOTATION_SOURCE_METHOD_NAMES = 
		"http://rcpviewer.berlios.de/progmodel/standard/methodNames";

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
	 * Key to EAnnotation details representing (names of) methods to access 
	 * a reference (single or collection).
	 * 
	 * <p>
	 * Holds key to the accessor method (always exists for references).
	 */
	public static final String ANNOTATION_REFERENCE_ACCESSOR_NAME_KEY = "get";

	/**
	 * Key to EAnnotation details representing (names of) method to associate
	 * instances with a reference.
	 * 
	 * <p>
	 * Holds key to the associate method, if it exists.
	 */
	public static final String ANNOTATION_REFERENCE_ASSOCIATOR_NAME_KEY = "associate";

	/**
	 * Key to EAnnotation details representing (names of) methods to dissociate
	 * instances with a reference.
	 * 
	 * <p>
	 * Holds key to the dissociate method, if it exists.
	 */
	public static final String ANNOTATION_REFERENCE_DISSOCIATOR_NAME_KEY = "dissociate";


	/**
	 * Prefix to the source of EAnnotations that represent extensions (adapters)
	 * for a model element.
	 * 
	 * <p>
	 * The prefix is appended with the fully qualified class name of the
	 * adapter class.
	 */
	public static final String ANNOTATION_EXTENSIONS_PREFIX = 
		"http://de.berlios.rcpviewer/progmodel/extensions/";

	/**
	 * Reserved key name for an element in details map of the EAnnotation on a
	 * model element holding extensions.
	 * 
	 * <p>
	 * Used to construct the {@link de.berlios.rcpviewer.domain.IAdapterFactory}
	 * that then interprets the remainder of the details to construct the actual
	 * adapter.
	 */
	public static final String ANNOTATION_EXTENSIONS_ADAPTER_FACTORY_NAME_KEY = 
			"adapterFactoryClassName";


}
