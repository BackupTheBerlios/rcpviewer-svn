/**
 * 
 */
package org.essentialplatform.core.deployment;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;

public interface IOneToOneReferenceBinding extends IReferenceBinding {
	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue);
}