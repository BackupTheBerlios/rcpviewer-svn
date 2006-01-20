package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.ICollectionReference;

/**
 * Represents runtime-specific functionality (common to client and server) for 
 * an <i>instance of</i> an {@link ICollectionReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectCollectionReferenceRuntimeBinding 
		extends IObjectReferenceRuntimeBinding {

	/**
	 * Hook method notifying the binding that an object has been added to the
	 * collection.
	 * 
	 * <p>
	 * The binding can perform context-specific processing (eg notifying
	 * listeners in the UI).
	 */
	public void addedToCollection();
	/**
	 * Hook method notifying the binding that an object has been added to the
	 * collection.
	 * 
	 * <p>
	 * The binding can perform context-specific processing (eg notifying
	 * listeners in the UI).
	 */
	public void removedFromCollection();

}