package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.ICollectionReference;
import org.essentialplatform.runtime.shared.domain.bindings.ICollectionReferenceRuntimeBinding;

/**
 * Represents server-specific functionality for an {@link ICollectionReference} 
 * of a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectCollectionReferenceClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface ICollectionReferenceServerBinding 
		extends ICollectionReferenceRuntimeBinding, IReferenceServerBinding {
}