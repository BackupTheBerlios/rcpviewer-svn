package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;

/**
 * Accepts all {@link IDomainClass.IOperation}.
 * 
 * @author Dan Haywood
 */
public final class NoopOperationFilter implements IFilter<IDomainClass.IOperation>{

	/**
	 * Always accepted.
	 *  
	 * @param member
	 * @return
	 */
	public boolean accept(IDomainClass.IOperation operation) {
		return true;
	}


}
