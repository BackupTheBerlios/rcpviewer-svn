package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;

/**
 * Accepts all {@link IDomainClass.IReference}.
 * 
 * @author Dan Haywood
 */
public final class NoopReferenceFilter implements IFilter<IDomainClass.IReference>{

	/**
	 * Always accepted.
	 *  
	 * @param member
	 * @return
	 */
	public boolean accept(IDomainClass.IReference reference) {
		return true;
	}


}
