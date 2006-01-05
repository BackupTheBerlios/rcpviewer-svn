/**
 * 
 */
package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;

public interface IReferenceClientBinding extends IReferenceRuntimeBinding {
	IPrerequisites authorizationPrerequisites();
	IPrerequisites accessorPrerequisitesFor(Object pojo);
}