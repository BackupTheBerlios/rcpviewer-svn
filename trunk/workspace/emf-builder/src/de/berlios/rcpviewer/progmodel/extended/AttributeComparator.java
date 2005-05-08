package de.berlios.rcpviewer.progmodel.extended;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.metamodel.EmfFacade;


/**
 * Extension of {@link IDomainClass}, derived from {@link de.berlios.rcpviewer.progmodel.extended.PositionedAt}
 * attribute, that can be used to compare attributes.
 * 
 * <p>
 * TODO: should really implement Comparator<EAttribute>, but AJDT 1.2m3 
 * compiler is throwing a null pointer exception.
 * 
 * @author dkhaywood
 *
 */
public final class AttributeComparator implements Comparator {

	/**
	 * Compares according to the {@link PositionedAt} attribute, if known, or
	 * the name if not known.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public int compare(Object arg0, Object arg1) {
		EAttribute attribute0 = (EAttribute)arg0;
		EAttribute attribute1 = (EAttribute)arg1;
		
		Map<String,String> attributeDetails0 = 
			emfFacade.getAnnotationDetails(attribute0, Constants.ANNOTATION_ATTRIBUTE);
		Map<String,String> attributeDetails1 = 
			emfFacade.getAnnotationDetails(attribute1, Constants.ANNOTATION_ATTRIBUTE);
		
		String positionedAtStr0 = 
			attributeDetails0.get(Constants.ANNOTATION_ATTRIBUTE_POSITIONED_AT_KEY);
		String positionedAtStr1 = 
			attributeDetails1.get(Constants.ANNOTATION_ATTRIBUTE_POSITIONED_AT_KEY);
		
		
		if (positionedAtStr0 != null && positionedAtStr1 == null) {
			// if have positioned #0 but not #1, then return #0 first.
			return -1; 
		} else if (positionedAtStr0 == null && positionedAtStr1 != null) {
			// if have positioned #1 but not #0, then return #1 first.
			return 1; 
		} else if (positionedAtStr0 == null || positionedAtStr1 == null) {
			// neither have been positioned
			return compareByName(attribute0, attribute1);
		} else {
			// both have been positioned
			int positionedAt0 = Integer.parseInt(positionedAtStr0);
			int positionedAt1 = Integer.parseInt(positionedAtStr1);
			if (positionedAt0 == positionedAt1) {
				// positioned at same place
				return compareByName(attribute0, attribute1);
			}
			// compare according to their position.
			return positionedAt1 - positionedAt0;
		}
	}
	
	private int compareByName(EAttribute attribute0, EAttribute attribute1) {
		return attribute0.getName().compareTo(attribute1.getName());
	}
	
	private final EmfFacade emfFacade = new EmfFacade();

	public List<EAttribute> compare(List<EAttribute> attributes) {
		Collections.sort(attributes, this);
		return attributes;
	}
	

}
