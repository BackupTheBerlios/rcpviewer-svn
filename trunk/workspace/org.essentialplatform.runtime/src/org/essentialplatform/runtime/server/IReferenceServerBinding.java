/**
 * 
 */
package org.essentialplatform.runtime.server;

import org.essentialplatform.core.deployment.IReferenceBinding;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;

public interface IReferenceServerBinding extends IReferenceBinding {
	/**
	 * Returns the pojo for a 1:1 reference, or a collection of pojos for
	 * a collection reference.
	 * 
	 * @param pojo
	 * @return
	 */
	Object invokeAccessor(Object pojo);
	void invokeAssociator(Object pojo, Object referencedPojo);
	boolean canAssociate();
	boolean canDissociate();
	void invokeDissociator(Object pojo, Object referencedPojo);
}