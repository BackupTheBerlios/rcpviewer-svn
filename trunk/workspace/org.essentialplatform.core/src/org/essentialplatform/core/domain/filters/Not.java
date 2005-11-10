package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;

/**
 * Inverts a filter; accepted if the supplied filter does not accept (and
 * vice versa).
 * 
 * @author Dan Haywood
 */
public final class Not<T extends IDomainClass.IMember> implements IFilter<T> {

	private IFilter<T> _filter;
	
	public Not(final IFilter<T> filter) {
		_filter = filter;
	}
	
	/**
	 * Accepted if the underlying filter does not.
	 * 
	 * @see org.essentialplatform.core.domain.filters.IFilter#accept(org.essentialplatform.core.domain.IDomainClass.IAttribute)
	 */
	public boolean accept(final T member) {
		return !_filter.accept(member);
	}

}
