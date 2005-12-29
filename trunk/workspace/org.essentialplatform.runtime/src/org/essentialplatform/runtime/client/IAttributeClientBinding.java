/**
 * 
 */
package org.essentialplatform.runtime.client;

import org.essentialplatform.core.deployment.IAttributeBinding;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;

public interface IAttributeClientBinding extends IAttributeBinding {
	Object invokeAccessor(Object pojo);
	void invokeMutator(Object pojo, Object newValue);
	IPrerequisites accessorPrerequisitesFor(Object pojo);
	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue);
}