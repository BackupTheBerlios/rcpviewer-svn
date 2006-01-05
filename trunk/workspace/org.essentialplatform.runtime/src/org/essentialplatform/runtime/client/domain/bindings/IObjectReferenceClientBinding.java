package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IReference;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectReferenceRuntimeBinding;

/**
 * Represents client-specific functionality for an <i>instance of</i> an 
 * {@link IReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectReferenceClientBinding 
		extends IObjectReferenceRuntimeBinding, IObjectMemberClientBinding {
}