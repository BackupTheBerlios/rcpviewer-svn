package org.essentialplatform.progmodel.essential.core.domain.comparators;

import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.comparators.AbstractIAttributeComparator;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelExtendedSemanticsConstants;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelExtendedSemanticsEmfSerializer;

public class IdAttributeComparator extends AbstractIAttributeComparator {

	private final EssentialProgModelExtendedSemanticsEmfSerializer _serializer = 
		new EssentialProgModelExtendedSemanticsEmfSerializer();

	/**
	 * Uses the value of the <tt>value</tt> property of the <tt>Id</tt> annotation.
	 */
	@Override
	protected Integer getPosition(IAttribute attribute) {
		return 
			_serializer.getAttributeAnnotationInt(
					attribute.getEAttribute(), 
					EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_ID_VALUE);
	}

}
