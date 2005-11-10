package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;

/**
 * Accepts only those operations that are static clsss operations (as opposed to
 * instance operations on a particular object). 
 * 
 * <p>
 * Use in combination with {@link IDomainClass#iOperations(IFilter<IOperation>)} 
 * to obtain the instance operations, eg:
 * <pre>
 * List<IOperation> staticOps = someDomainClass.iOperations(new StaticOperationFilter());
 * </pre>
 * 
 * <p>
 * To obtain static operations, either use the {@link InstanceOperationFilter} or
 * combine this filter with the {@link Not} filter, eg:
 * <pre>
 * List<IOperation> instanceOps = someDomainClass.iOperations(
 * 										new Not(new StaticOperationFilter()));
 * </pre>
 * 
 * 
 * @author Dan Haywood
 */
public final class StaticOperationFilter implements IFilter<IDomainClass.IOperation>{

	/**
	 * Accepted only if a static operation.
	 *  
	 * @param member
	 * @return
	 */
	public boolean accept(IDomainClass.IOperation member) {
		return member.isStatic();
	}


}
