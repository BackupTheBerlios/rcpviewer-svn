/**
 * 
 */
package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.deployment.IReferenceBinding;

public interface IReferenceRuntimeBinding extends IReferenceBinding, IMemberRuntimeBinding {
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