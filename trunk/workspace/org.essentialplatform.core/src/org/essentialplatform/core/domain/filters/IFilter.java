package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.IDomainClass.IMember;

/**
 * Filter {@link IDomainClass.IMember}s such that a subset is returned.
 * 
 * @author Dan Haywood
 */
public interface IFilter<T> {
	
	/**
	 * Whether this object should be accepted or not.
	 *  
	 * @param member
	 * @return <tt>true</tt> if it should be accepted.
	 */
	public boolean accept(T member);
	
}
