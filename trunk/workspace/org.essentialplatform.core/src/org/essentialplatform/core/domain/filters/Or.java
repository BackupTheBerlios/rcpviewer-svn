package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.IDomainClass.IMember;
import org.essentialplatform.core.domain.builders.IDomainBuilder;

/**
 * Combines filters; any must apply to be accepted.
 * 
 * @author Dan Haywood
 */
public final class Or<T extends IDomainClass.IMember> implements IFilter<T> {

	private IFilter<T>[] _filters;
	
	public Or(final IFilter<T>... filters) {
		_filters = filters;
	}
	
	/**
	 * Accepted if any filters accept.
	 * 
	 * @see org.essentialplatform.core.domain.filters.IFilter#accept(org.essentialplatform.core.domain.IDomainClass.IAttribute)
	 */
	public boolean accept(final T member) {
		for(IFilter<T> filter: _filters) {
			if (filter.accept(member)) {
				return true;
			}
		}
		return false;
	}

}
