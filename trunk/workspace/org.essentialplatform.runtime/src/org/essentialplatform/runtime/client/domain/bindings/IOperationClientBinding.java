/**
 * 
 */
package org.essentialplatform.runtime.client.domain.bindings;

import java.util.Map;

import org.essentialplatform.core.deployment.IOperationBinding;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;

public interface IOperationClientBinding extends IOperationBinding {

	Object invokeOperation(Object pojo, Object[] args);

	void assertIsValid(int position, Object arg);

	Object[] getArgs(Map<Integer, Object> argsByPosition);

	IPrerequisites prerequisitesFor(Object pojo, Object[] args);

	Object[] reset(Object pojo, Object[] args, Map<Integer, Object> argsByPosition); 
}