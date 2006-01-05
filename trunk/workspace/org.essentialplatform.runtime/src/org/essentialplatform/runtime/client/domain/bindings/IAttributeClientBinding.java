/**
 * 
 */
package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.bindings.IAttributeRuntimeBinding;

public interface IAttributeClientBinding extends IAttributeRuntimeBinding {
	IPrerequisites accessorPrerequisitesFor(Object pojo);
	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue);
}