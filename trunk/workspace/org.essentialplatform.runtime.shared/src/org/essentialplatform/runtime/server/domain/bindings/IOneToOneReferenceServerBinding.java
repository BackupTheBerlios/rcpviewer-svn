/**
 * 
 */
package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.runtime.shared.domain.bindings.IOneToOneReferenceRuntimeBinding;

/**
 * Represents server-specific functionality for an {@link IOneToOneReference} of 
 * a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectOneToOneReferenceServerBinding. 
 * 
 * @author Dan Haywood
 */

public interface IOneToOneReferenceServerBinding 
		extends IOneToOneReferenceRuntimeBinding, IReferenceServerBinding {
}