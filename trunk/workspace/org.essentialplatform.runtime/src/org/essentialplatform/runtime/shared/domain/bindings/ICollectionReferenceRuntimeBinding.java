package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.deployment.ICollectionReferenceBinding;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.ICollectionReference;

/**
 * Represents runtime-specific functionality for an {@link ICollectionReference} of 
 * a {@link IDomainClass} (common to both client- and server).
 * 
 * <p>
 * @see IObjectCollectionReferenceRuntimeBinding. 
 * 
 * @author Dan Haywood
 */
public interface ICollectionReferenceRuntimeBinding 
		extends ICollectionReferenceBinding, IReferenceRuntimeBinding {
}