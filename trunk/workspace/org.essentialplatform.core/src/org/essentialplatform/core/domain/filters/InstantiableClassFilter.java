package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;

/**
 * Accepts only those {@link IDomainClass} that are instantiable.
 * 
 * <p>
 * Use in combination with {@link IDomain#iClasses(IFilter<IDomainClass>)} 
 * to obtain the instantiable classes, eg:
 * <pre>
 * List<IDomainClass> instantiableClasses = 
 * 	   someDomain.iClasses(new InstantiableClassFilter());
 * </pre>
 * 
 * @author Dan Haywood
 */
public final class InstantiableClassFilter implements IFilter<IDomainClass>{

	/**
	 * Accepted only if an Id attribute.
	 *  
	 * @param member
	 * @return
	 */
	public boolean accept(IDomainClass domainClass) {
		return domainClass.isInstantiable();
	}


}
