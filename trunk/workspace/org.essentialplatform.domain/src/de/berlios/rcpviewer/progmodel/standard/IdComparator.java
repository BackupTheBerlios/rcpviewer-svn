package de.berlios.rcpviewer.progmodel.standard;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.domain.EmfAnnotations;
import de.berlios.rcpviewer.progmodel.extended.Id;

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
		super(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_ID_VALUE);
	}


}
