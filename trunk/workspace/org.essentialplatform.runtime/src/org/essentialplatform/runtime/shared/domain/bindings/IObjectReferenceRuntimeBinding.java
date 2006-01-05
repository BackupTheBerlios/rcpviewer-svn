package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IReference;

/**
 * Represents runtime-specific functionality (common to client and server) for 
 * an <i>instance of</i> an {@link IReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectReferenceRuntimeBinding 
		extends IObjectMemberRuntimeBinding {
}