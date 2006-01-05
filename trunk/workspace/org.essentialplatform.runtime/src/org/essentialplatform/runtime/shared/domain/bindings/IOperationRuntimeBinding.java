package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.deployment.IOperationBinding;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IOperation;

/**
 * Represents runtime-specific functionality for an {@link IOperation} of 
 * a {@link IDomainClass} (common to both client- and server).
 * 
 * <p>
 * @see IObjectOperationRuntimeBinding. 
 * 
 * @author Dan Haywood
 */
public interface IOperationRuntimeBinding extends IOperationBinding, IMemberRuntimeBinding {

}