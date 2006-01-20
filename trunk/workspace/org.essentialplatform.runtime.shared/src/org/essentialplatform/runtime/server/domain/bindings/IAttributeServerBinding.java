package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.runtime.shared.domain.bindings.IAttributeRuntimeBinding;


/**
 * Represents server-specific functionality for an {@link IAttribute} of 
 * a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectAttributeServerBinding. 
 * 
 * @author Dan Haywood
 */
public interface IAttributeServerBinding extends IAttributeRuntimeBinding, IMemberServerBinding {
}
