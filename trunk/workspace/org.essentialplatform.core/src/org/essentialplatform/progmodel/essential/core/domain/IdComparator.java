package org.essentialplatform.progmodel.essential.core.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;

import org.essentialplatform.core.emf.EmfAnnotations;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelExtendedSemanticsConstants;
import org.essentialplatform.progmodel.extended.Id;

/**
 * Extension of {@link IDomainClass}, derived from {@link RelativeOrder}
 * attribute, that can be used to compare attributes according to their 
 * ({@link Id}.
 * 
 * @author dkhaywood
 *
 */
public final class IdComparator extends AbstractAttributeComparator {

	public IdComparator() {
		super(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_ID_VALUE);
	}


}
