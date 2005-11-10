package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;

/**
 * Accepts only those attributes that are part of the id of the 
 * {@link IDomainClass} that they belong to (eg as defined by an <tt>@Id</tt>
 * annotation).
 * 
 * <p>
 * Use in combination with {@link IDomainClass#iAttributes(IFilter<IAttribute>)} 
 * to obtain the id attributes, eg:
 * <pre>
 * List<IAttribute> idAttribs = someDomainClass.iAttributes(new IdAttributeFilter());
 * </pre>
 * 
 * @author Dan Haywood
 */
public final class IdAttributeFilter implements IFilter<IDomainClass.IAttribute>{

	/**
	 * Accepted only if an Id attribute.
	 *  
	 * @param member
	 * @return
	 */
	public boolean accept(IAttribute member) {
		return member.isId();
	}


}
