package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.IDomainClass.IMember;
import org.essentialplatform.core.domain.builders.IDomainBuilder;

/**
 * Combines filters; all must apply to be accepted.
 * 
 * @author Dan Haywood
 */
public final class And<T> implements IFilter<T> {

	private IFilter<T>[] _filters;
	
	public And(final IFilter<T>... filters) {
		_filters = filters;
	}
	
	/**
	 * Accepted only if all filters accept.
	 * 
	 * @see org.essentialplatform.core.domain.filters.IFilter#accept(org.essentialplatform.core.domain.IDomainClass.IAttribute)
	 */
	public boolean accept(final T member) {
		for(IFilter<T> filter: _filters) {
			if (!filter.accept(member)) {
				return false;
			}
		}
		return true;
	}

}
