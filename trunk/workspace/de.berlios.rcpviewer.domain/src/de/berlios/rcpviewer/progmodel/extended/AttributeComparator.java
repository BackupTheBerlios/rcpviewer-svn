package de.berlios.rcpviewer.progmodel.extended;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import de.berlios.rcpviewer.progmodel.extended.RelativeOrder;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.domain.EmfAnnotations;


/**
 * Extension of {@link IDomainClass}, derived from {@link RelativeOrder}
 * attribute, that can be used to compare attributes according to their 
 * ({@link RelativeOrder}.
 * 
 * @author dkhaywood
 *
 */
final class AttributeComparator extends AbstractAttributeComparator {

	protected AttributeComparator() {
		super(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_RELATIVE_ORDER_KEY);
	}

}
