package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;

/**
 * Accepts all {@link IDomainClass.IAttribute}.
 * 
 * @author Dan Haywood
 */
public final class NoopAttributeFilter implements IFilter<IDomainClass.IAttribute>{

	/**
	 * Always accepted.
	 *  
	 * @param member
	 * @return
	 */
	public boolean accept(IDomainClass.IAttribute attribute) {
		return true;
	}


}
