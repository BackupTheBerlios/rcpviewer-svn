package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectAttributeRuntimeBinding;

/**
 * Represents client-specific functionality for an <i>instance of</i> an
 * {@link IAttribute} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectAttributeClientBinding extends IObjectAttributeRuntimeBinding, IObjectMemberClientBinding {
}