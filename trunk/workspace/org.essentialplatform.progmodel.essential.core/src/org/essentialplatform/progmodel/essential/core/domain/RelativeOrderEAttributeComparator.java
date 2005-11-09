package org.essentialplatform.progmodel.essential.core.domain;

import org.essentialplatform.core.domain.AbstractEAttributeComparator;
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
public final class RelativeOrderEAttributeComparator extends AbstractEAttributeComparator {

	public RelativeOrderEAttributeComparator() {
		super(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_RELATIVE_ORDER_KEY);
	}

}
