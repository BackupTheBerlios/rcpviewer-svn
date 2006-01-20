package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass.IReference;
import org.essentialplatform.runtime.shared.domain.bindings.IReferenceRuntimeBinding;

/**
 * Represents server-specific functionality for an {@link IReference} of 
 * a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectReferenceServerBinding. 
 * 
 * @author Dan Haywood
 */
public interface IReferenceServerBinding extends IReferenceRuntimeBinding, IMemberServerBinding {
}