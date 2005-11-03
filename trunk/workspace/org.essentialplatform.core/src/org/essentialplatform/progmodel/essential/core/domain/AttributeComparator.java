package org.essentialplatform.progmodel.essential.core.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.essentialplatform.progmodel.essential.core.EssentialProgModelExtendedSemanticsConstants;
import org.essentialplatform.progmodel.extended.RelativeOrder;

import org.eclipse.emf.ecore.EAttribute;

import org.essentialplatform.core.emf.EmfAnnotations;


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
