package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IOperation;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectOperationRuntimeBinding;

/**
 * Represents server-specific functionality for an <i>instance of</i> an 
 * {@link IOperation} of a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectOperationClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface IObjectOperationServerBinding 
		extends IObjectOperationRuntimeBinding, IObjectMemberServerBinding {

}