package org.essentialplatform.progmodel.essential.core.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;

import org.essentialplatform.core.domain.AbstractEAttributeComparator;
import org.essentialplatform.core.emf.EmfAnnotations;
import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelExtendedSemanticsConstants;

/**
 * Extension of {@link IDomainClass}, derived from {@link RelativeOrder}
 * attribute, that can be used to compare attributes according to their 
 * ({@link Id}.
 * 
 * @author dkhaywood
 *
 */
public final class IdEAttributeComparator extends AbstractEAttributeComparator {

	public IdEAttributeComparator() {
		super(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_ID_VALUE);
	}


}
