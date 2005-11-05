package org.essentialplatform.progmodel.louis.core;


/**
 * Set of constants relating to this package.
 * 
 * @author Dan Haywood
 */
public final class LouisProgModelConstants {

	
	/**
	 * Cannot instantiate.
	 */
	private LouisProgModelConstants() {
	}

	/**
	 * Presence of an EAnnotation with this source on any EModelElement 
	 * indicates additional information accessible from the details.
	 */
	public static final String ANNOTATION_ELEMENT = 
		"http://rcpviewer.berlios.de/progmodel/rcpviewer/element";

	/**
	 * Presence of an EAnnotation with this source on an EClass indicates
	 * additional information accessible from the details.
	 */
	public static final String ANNOTATION_CLASS = 
		"http://rcpviewer.berlios.de/progmodel/rcpviewer/class";

	/**
	 * Presence of an EAnnotation with this source on an EAttribute indicates
	 * additional information accessible from the details.
	 */
	public static final String ANNOTATION_ATTRIBUTE = 
		"http://rcpviewer.berlios.de/progmodel/rcpviewer/attribute";

	/**
	 * Presence of an EAnnotation with this source on an EReference indicates
	 * additional information accessible from the details.
	 */
	public static final String ANNOTATION_REFERENCE = 
		"http://rcpviewer.berlios.de/progmodel/rcpviewer/reference";

	/**
	 * Presence of an EAnnotation with this source on an EOperation indicates
	 * additional information accessible from the details.
	 */
	public static final String ANNOTATION_OPERATION = 
		"http://rcpviewer.berlios.de/progmodel/rcpviewer/operation";

	/**
	 * Presence of an EAnnotation with this source on an EParameter indicates
	 * additional information accessible from the details.
	 */
	public static final String ANNOTATION_OPERATION_PARAMETER = 
		"http://rcpviewer.berlios.de/progmodel/rcpviewer/operation/parameter";


	/**
	 * Key to details of EAnnotation with source of {@link #ANNOTATION_ELEMENT}
	 * whose value is the description of the annotated model element.
	 */
	public static final String ANNOTATION_CLASS_IMAGE_URL_AT_KEY = 
		"imageUrlAt";
	

}
