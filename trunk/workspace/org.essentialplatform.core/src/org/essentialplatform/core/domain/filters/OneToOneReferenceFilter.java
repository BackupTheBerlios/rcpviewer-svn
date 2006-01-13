package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IReference;

/**
 * Accepts only 1:1 references.
 * 
 * @author Dan Haywood
 */
public final class OneToOneReferenceFilter implements IFilter<IDomainClass.IReference>{

	/**
	 * Accepted if not {@link IReference#isMultiple()}
	 * 
	 *  
	 * @param member
	 * @return
	 */
	public boolean accept(IReference reference) {
		return !reference.isMultiple();
	}


}
