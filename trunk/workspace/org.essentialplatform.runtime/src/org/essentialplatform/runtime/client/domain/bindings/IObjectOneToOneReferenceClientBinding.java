package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IOneToOneReference;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectOneToOneReferenceRuntimeBinding;

/**
 * Represents client-specific functionality for an <i>instance of</i> an 
 * {@link IOneToOneReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectOneToOneReferenceClientBinding 
		extends IObjectOneToOneReferenceRuntimeBinding, IObjectReferenceClientBinding {
}