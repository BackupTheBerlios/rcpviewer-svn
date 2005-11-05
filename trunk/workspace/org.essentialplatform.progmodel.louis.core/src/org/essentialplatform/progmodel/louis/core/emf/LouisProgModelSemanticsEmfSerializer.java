package org.essentialplatform.progmodel.louis.core.emf;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import sun.security.krb5.internal.crypto.b;

import org.essentialplatform.core.emf.EmfAnnotations;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelStandardSemanticsConstants;
import org.essentialplatform.progmodel.louis.core.LouisProgModelConstants;
import org.essentialplatform.progmodel.rcpviewer.ImageUrlAt;

/**
 * Serializes and deserializes semantics for the rcpviewer programming model
 * to and from the EMF model.
 *  
 * @author Dan Haywood
 */
public final class LouisProgModelSemanticsEmfSerializer {

	private final EmfAnnotations _emfAnnotations = new EmfAnnotations();

	// class

	public ImageUrlAt getClassImageUrlAt(EClass eClass) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(eClass, LouisProgModelConstants.ANNOTATION_CLASS);
		final String imageUrlAt = attributeDetails.get(LouisProgModelConstants.ANNOTATION_CLASS_IMAGE_URL_AT_KEY);
		if (imageUrlAt == null) return null;
		return ImageUrlAt.Factory.create(imageUrlAt);
	}
	public void setClassImageUrlAt(EClass eClass, ImageUrlAt imageUrlAt) {
		if (imageUrlAt == null) return;
		_emfAnnotations.putAnnotationDetails(
				eClass, 
				LouisProgModelConstants.ANNOTATION_CLASS, 
				LouisProgModelConstants.ANNOTATION_CLASS_IMAGE_URL_AT_KEY, 
				imageUrlAt.value());	
	}
}
