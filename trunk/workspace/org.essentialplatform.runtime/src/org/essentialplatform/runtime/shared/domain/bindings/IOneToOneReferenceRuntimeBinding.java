package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.deployment.IOneToOneReferenceBinding;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IOneToOneReference;

/**
 * Represents runtime-specific functionality for an {@link IOneToOneReference} of 
 * a {@link IDomainClass} (common to both client- and server).
 * 
 * <p>
 * @see IObjectOneToOneReferenceRuntimeBinding. 
 * 
 * @author Dan Haywood
 */
public interface IOneToOneReferenceRuntimeBinding 
		extends IOneToOneReferenceBinding, IReferenceRuntimeBinding {
}