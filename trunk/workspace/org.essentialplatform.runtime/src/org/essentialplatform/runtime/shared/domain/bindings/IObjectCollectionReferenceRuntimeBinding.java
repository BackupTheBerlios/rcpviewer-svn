package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.ICollectionReference;

/**
 * Represents runtime-specific functionality (common to client and server) for 
 * an <i>instance of</i> an {@link ICollectionReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectCollectionReferenceRuntimeBinding 
		extends IObjectReferenceRuntimeBinding {
}