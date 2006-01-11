package org.essentialplatform.runtime.client.domain.bindings;

import java.util.Map;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOperation;
import org.essentialplatform.runtime.shared.domain.bindings.IOperationRuntimeBinding;

/**
 * Represents client-specific functionality for an {@link IOperation} of 
 * a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectOperationClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface IOperationClientBinding extends IOperationRuntimeBinding, IMemberClientBinding {

	Object invokeOperation(Object pojo, Object[] args);

	void assertIsValid(int position, Object arg);

	Object[] getArgs(Map<Integer, Object> argsByPosition);

	IPrerequisites prerequisitesFor(Object pojo, Object[] args);

	Object[] reset(Object pojo, Object[] args, Map<Integer, Object> argsByPosition);

}