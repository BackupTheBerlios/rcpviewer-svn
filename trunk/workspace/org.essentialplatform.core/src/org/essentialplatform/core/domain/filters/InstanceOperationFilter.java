package org.essentialplatform.core.domain.filters;

import org.essentialplatform.core.domain.IDomainClass;

/**
 * Accepts only those operations that are inatance operations (as opposed to
 * static class operations). 
 * 
 * <p>
 * Use in combination with {@link IDomainClass#iOperations(IFilter<IOperation>)} 
 * to obtain the instance operations, eg:
 * <pre>
 * List<IOperation> instanceOps = someDomainClass.iOperations(new InstanceOperationFilter());
 * </pre>
 * 
 * <p>
 * To obtain static operations, either use the {@link StaticOperationFilter} or
 * combine this filter with the {@link Not} filter, eg:
 * <pre>
 * List<IOperation> staticOps = someDomainClass.iOperations(
 * 										new Not(new InstanceOperationFilter()));
 * </pre>
 * 
 * 
 * @author Dan Haywood
 */
public final class InstanceOperationFilter implements IFilter<IDomainClass.IOperation>{

	/**
	 * Accepted only if a non-static operation.
	 *  
	 * @param member
	 * @return
	 */
	public boolean accept(IDomainClass.IOperation member) {
		return !member.isStatic();
	}


}
