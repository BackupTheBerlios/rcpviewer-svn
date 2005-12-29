/**
 * 
 */
package org.essentialplatform.runtime.client;

import org.essentialplatform.core.deployment.ICollectionReferenceBinding;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;

public interface ICollectionReferenceClientBinding 
		extends ICollectionReferenceBinding, IReferenceClientBinding {
	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue, boolean beingAdded);
}