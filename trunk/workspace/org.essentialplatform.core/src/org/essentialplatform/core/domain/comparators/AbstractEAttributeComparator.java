package org.essentialplatform.core.domain.comparators;

import java.util.Comparator;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelExtendedSemanticsEmfSerializer;

/**
 * Compare attributes according to an annotation. 
 * 
 * @author dkhaywood
 *
 */
public abstract class AbstractEAttributeComparator implements Comparator<EAttribute> {

	private final EssentialProgModelExtendedSemanticsEmfSerializer _serializer = new EssentialProgModelExtendedSemanticsEmfSerializer();


	private final String _annotationAttributeKey;

	
	/**
	 * 
	 * @param annotationAttributeKey - ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_?????_KEY
	 */
	protected AbstractEAttributeComparator(final String annotationAttributeKey) {
		_annotationAttributeKey = annotationAttributeKey;
	}
	
	/**
	 * Compare according to attribute key provided in constructor.
	 * 
	 * @param attribute0
	 * @param attribute1
	 * @return
	 */
	public int compare(EAttribute attribute0, EAttribute attribute1) {
		Integer position0 = 
			_serializer.getAttributeAnnotationInt(attribute0, _annotationAttributeKey);
		Integer position1 = 
			_serializer.getAttributeAnnotationInt(attribute1, _annotationAttributeKey);
		
		if (position0 != null && position1 == null) {
			// if have positioned #0 but not #1, then return #0 first.
			return -1; 
		} else if (position0 == null && position1 != null) {
			// if have positioned #1 but not #0, then return #1 first.
			return 1; 
		} else if (position0 == null || position1 == null) {
			// neither have been positioned
			return compareByName(attribute0, attribute1);
		} else {
			// both have been positioned
			if (position0.intValue() == position1.intValue()) {
				// positioned at same place
				return compareByName(attribute0, attribute1);
			} else
			if (position0.intValue() < position1.intValue()) {
				return -1;
			} else {
				// position0 > position1
				return +1;
			}
		}
	}
	
	private int compareByName(EAttribute attribute0, EAttribute attribute1) {
		return attribute0.getName().compareTo(attribute1.getName());
	}
	

}
