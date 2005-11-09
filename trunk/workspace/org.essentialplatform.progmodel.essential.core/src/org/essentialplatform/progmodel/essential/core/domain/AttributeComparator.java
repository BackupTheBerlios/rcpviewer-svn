package org.essentialplatform.progmodel.essential.core.domain;

import org.essentialplatform.core.domain.AbstractAttributeComparator;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.progmodel.essential.app.RelativeOrder;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelExtendedSemanticsConstants;


/**
 * Extension of {@link IDomainClass}, derived from {@link RelativeOrder}
 * attribute, that can be used to compare attributes according to their 
 * ({@link RelativeOrder}.
 * 
 * @author dkhaywood
 *
 */
public final class AttributeComparator extends AbstractAttributeComparator {

	public AttributeComparator() {
		super(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_RELATIVE_ORDER_KEY);
	}

}
