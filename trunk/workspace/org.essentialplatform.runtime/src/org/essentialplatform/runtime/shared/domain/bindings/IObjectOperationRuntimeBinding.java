package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IOperation;

/**
 * Represents runtime-specific functionality (common to client and server) for 
 * an <i>instance of</i> an {@link IOperation} of a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectOperationClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface IObjectOperationRuntimeBinding 
		extends IObjectMemberRuntimeBinding {
}