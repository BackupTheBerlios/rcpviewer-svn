package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.ICollectionReference;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectCollectionReferenceRuntimeBinding;

/**
 * Represents client-specific functionality for an <i>instance of</i> an 
 * {@link ICollectionReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectCollectionReferenceClientBinding 
		extends IObjectCollectionReferenceRuntimeBinding, IObjectReferenceClientBinding {
}