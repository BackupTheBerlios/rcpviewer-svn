/**
 * 
 */
package org.essentialplatform.runtime.client;

import org.essentialplatform.core.deployment.IOneToOneReferenceBinding;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;

public interface IOneToOneReferenceClientBinding 
		extends IOneToOneReferenceBinding, IReferenceClientBinding {
	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue);
}