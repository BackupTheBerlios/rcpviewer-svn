/**
 * 
 */
package org.essentialplatform.core.deployment;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;

public interface ICollectionReferenceClientBinding extends IReferenceClientBinding {
	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue, boolean beingAdded);
}