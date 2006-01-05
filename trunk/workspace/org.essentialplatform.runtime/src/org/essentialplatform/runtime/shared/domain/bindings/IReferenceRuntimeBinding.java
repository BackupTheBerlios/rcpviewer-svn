package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.deployment.IReferenceBinding;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IReference;

/**
 * Represents runtime-specific functionality for an {@link IReference} of 
 * a {@link IDomainClass} (common to both client- and server).
 * 
 * <p>
 * @see IObjectReferenceRuntimeBinding. 
 * 
 * @author Dan Haywood
 */
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