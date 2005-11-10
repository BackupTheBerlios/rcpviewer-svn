package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;

/**
 * Accepts all {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public final class NoopClassFilter implements IFilter<IDomainClass>{

	/**
	 * Always accepted.
	 *  
	 * @param member
	 * @return
	 */
	public boolean accept(IDomainClass domainClass) {
		return true;
	}


}
