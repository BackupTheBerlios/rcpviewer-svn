/**
 * 
 */
package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.bindings.IOneToOneReferenceRuntimeBinding;

public interface IOneToOneReferenceClientBinding 
		extends IOneToOneReferenceRuntimeBinding, IReferenceClientBinding {
	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue);
}