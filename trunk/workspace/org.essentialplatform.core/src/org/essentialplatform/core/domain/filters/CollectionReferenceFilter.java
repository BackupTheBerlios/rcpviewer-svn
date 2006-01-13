package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IReference;

/**
 * Accepts only collection references.
 * 
 * @author Dan Haywood
 */
public final class CollectionReferenceFilter implements IFilter<IDomainClass.IReference>{

	/**
	 * Accepted only if {@link IReference#isMultiple()}
	 * 
	 *  
	 * @param member
	 * @return
	 */
	public boolean accept(IReference reference) {
		return reference.isMultiple();
	}


}
