/**
 * 
 */
package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.bindings.ICollectionReferenceRuntimeBinding;

public interface ICollectionReferenceClientBinding 
		extends ICollectionReferenceRuntimeBinding, IReferenceClientBinding {
	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue, boolean beingAdded);
}