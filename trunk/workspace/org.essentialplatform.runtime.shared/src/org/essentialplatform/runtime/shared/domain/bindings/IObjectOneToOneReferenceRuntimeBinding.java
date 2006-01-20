package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IOneToOneReference;

/**
 * Represents runtime-specific functionality (common to client and server) for 
 * an <i>instance of</i> an {@link IOneToOneReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectOneToOneReferenceRuntimeBinding 
		extends IObjectReferenceRuntimeBinding {
	
	
	/**
	 * Hook method for the binding that the 1:1 reference has been set 
	 * (either to a new object or null) so that the binding can perform
	 * binding-specific functionality.
	 * 
	 * <p>
	 * For example, client-side binding might notify an listeners in the UI. 
	 * 
	 * @param newReferencedObjectOrNull
	 */
	public void set(Object newReferencedObjectOrNull);
	
}