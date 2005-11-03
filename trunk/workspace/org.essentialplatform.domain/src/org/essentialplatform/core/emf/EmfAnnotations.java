package org.essentialplatform.core.emf;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelStandardSemanticsConstants;
import org.essentialplatform.progmodel.essential.core.util.EssentialProgModelStandardSemanticsRules;

/**
 * Misnamed: is now support for serializing EMF annotations.
 *
 * <p>
 * The {@link Emf} class now takes over the job of representing the EMF resource set.
 * 
 * @author Dan Haywood
 */
public final class EmfAnnotations {

	
	/**
	 * Returns the {@link EAnnotation} of the supplied source on the model element,
	 * creating it if necessary.
	 * 
	 * @param modelElement
	 * @param annotationSource
	 * @return
	 */
	public EAnnotation annotationOf(EModelElement eModelElement, String annotationSource) {
		EAnnotation eAnnotation = eModelElement.getEAnnotation(annotationSource);
		if (eAnnotation == null) {
			eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
			eAnnotation.setSource(annotationSource);
			eAnnotation.setEModelElement(eModelElement);
		}
		return eAnnotation;
	}
	

	public EAnnotation putAnnotationDetails(EAnnotation eAnnotation, Map<String, String> details) {
		eAnnotation.getDetails().putAll(details);
		return eAnnotation;
	}

	public void putAnnotationDetails(EModelElement eModelElement, String annotationSource, Map<String, String> details) {
		annotationOf(eModelElement, annotationSource).getDetails().putAll(details);
	}

	public Map<String, String> getAnnotationDetails(EAnnotation eAnnotation) {
		Map<String, String> retrievedDetails = new HashMap<String,String>();
		EMap details = eAnnotation.getDetails();
		for(Object key: details.keySet()) {
			retrievedDetails.put((String)key, (String)details.get(key));
		}
		return retrievedDetails;
	}
	
	public Map<String, String> getAnnotationDetails(EModelElement eModelElement, String annotationSource) {
		Map<String, String> retrievedDetails = new HashMap<String,String>();
		EMap details = annotationOf(eModelElement, annotationSource).getDetails();
		for(Object key: details.keySet()) {
			retrievedDetails.put((String)key, (String)details.get(key));
		}
		return retrievedDetails;
	}

	/**
	 * Returns all annotations of the element.
	 * 
	 * <p>
	 * The returned list is a copy and may safely be modified by the caller.
	 *  
	 * @param eModelElement
	 * @return
	 */
	public List<EAnnotation> annotationsOf(EModelElement eModelElement) {
		return annotationsPrefixed(eModelElement, null);
	}
	
	/**
	 * Returns all annotations of the element whose source has a prefix that
	 * matches the supplied annotation source prefix. 
	 * 
	 * <p>
	 * The returned list is a copy and may safely be modified by the caller.
	 *  
	 * @param eModelElement
	 * @return
	 */
	public List<EAnnotation> annotationsPrefixed(EModelElement eModelElement, String annotationPrefix) {
		List<EAnnotation> annotations = new ArrayList<EAnnotation>();
		annotations.addAll(eModelElement.getEAnnotations());
		Iterator<EAnnotation> iter = annotations.iterator();
		while(iter.hasNext()) {
			EAnnotation annotation = iter.next();
			if (!annotation.getSource().startsWith(annotationPrefix)) {
				iter.remove();
			}
		}
		return annotations;
	}

	EAnnotation putMethodNameIn(IDomainClass domainClass, EAnnotation eAnnotation, String methodKey, String methodName) {
		return putAnnotationDetails(domainClass, eAnnotation, methodKey, methodName);
	}

	public String getAnnotationDetail(EAnnotation eAnnotation, String key) {
		return (String)getAnnotationDetails(eAnnotation).get(key);
	}
	
	public EAnnotation putAnnotationDetails(IDomainClass domainClass, EAnnotation eAnnotation, String key, String value) {
		return putAnnotationDetails(eAnnotation, key, value);
	}

	public EAnnotation putAnnotationDetails(EAnnotation eAnnotation, String key, String value) {
		Map<String, String> details = new HashMap<String, String>();
		details.put(key, value);
		return putAnnotationDetails(eAnnotation, details);
	}

	public void putAnnotationDetails(IDomainClass domainClass, EModelElement modelElement, String key, boolean value) {
		putAnnotationDetails(modelElement, EssentialProgModelStandardSemanticsConstants.ANNOTATION_ELEMENT, key, value?"true":"false");
	}

	public void putAnnotationDetails(EModelElement modelElement, String annotationSource, String key, String value) {
		putAnnotationDetails(annotationOf(modelElement, annotationSource), key, value);
	}

	/**
	 * Puts annotation details for the model element using the source
	 * {@link EssentialProgModelStandardSemanticsConstants#ANNOTATION_ELEMENT}.
	 * 
	 * @param domainClass
	 * @param modelElement
	 * @param key
	 * @param value
	 */
	public void putAnnotationDetails(IDomainClass domainClass, EModelElement modelElement, String key, String value) {
		putAnnotationDetails(domainClass, modelElement, EssentialProgModelStandardSemanticsConstants.ANNOTATION_ELEMENT, key, value);
	}

	public void putAnnotationDetails(IDomainClass domainClass, EModelElement modelElement, String annotationSource, String key, String value) {
		EAnnotation ea = modelElement.getEAnnotation(annotationSource);
		if (ea == null) {
			ea = annotationOf(modelElement, annotationSource);
		}
		putAnnotationDetails(ea, key, value);
	}

	public EAnnotation methodNamesAnnotationFor(EModelElement eModelElement) {
		return annotationFor(eModelElement, EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES);
	}

	public EAnnotation annotationFor(EModelElement eModelElement, final String annotationSource) {
		EAnnotation eAnnotation = 
			eModelElement.getEAnnotation(annotationSource);
		if (eAnnotation != null) {
			return eAnnotation;
		}
		return annotationOf(eModelElement, annotationSource);
	}

}
