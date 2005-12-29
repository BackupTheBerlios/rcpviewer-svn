/**
 * 
 */
package org.essentialplatform.core.deployment;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;

public interface IAttributeClientBinding {
	Object invokeAccessor(Object pojo);
	void invokeMutator(Object pojo, Object newValue);
	IPrerequisites accessorPrerequisitesFor(Object pojo);
	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue);
}