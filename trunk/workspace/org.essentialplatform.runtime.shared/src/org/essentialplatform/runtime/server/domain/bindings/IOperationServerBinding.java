package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.runtime.shared.domain.bindings.IOperationRuntimeBinding;


/**
 * Represents server-specific functionality for an {@link IOperation} of 
 * a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectOperationServerBinding. 
 * 
 * @author Dan Haywood
 */
public interface IOperationServerBinding extends IOperationRuntimeBinding, IMemberServerBinding {

}