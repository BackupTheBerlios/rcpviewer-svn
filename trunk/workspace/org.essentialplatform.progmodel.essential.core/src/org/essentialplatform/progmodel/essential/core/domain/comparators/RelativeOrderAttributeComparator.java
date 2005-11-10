package org.essentialplatform.progmodel.essential.core.domain.comparators;

import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.comparators.AbstractIAttributeComparator;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelExtendedSemanticsConstants;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelExtendedSemanticsEmfSerializer;

public class RelativeOrderAttributeComparator extends AbstractIAttributeComparator {

	private final EssentialProgModelExtendedSemanticsEmfSerializer _serializer = 
		new EssentialProgModelExtendedSemanticsEmfSerializer();

	/**
	 * Uses the value of the relative order key.
	 */
	@Override
	protected Integer getPosition(IAttribute attribute) {
		return 
			_serializer.getAttributeAnnotationInt(
					attribute.getEAttribute(), 
					EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_RELATIVE_ORDER_KEY);
	}


}
