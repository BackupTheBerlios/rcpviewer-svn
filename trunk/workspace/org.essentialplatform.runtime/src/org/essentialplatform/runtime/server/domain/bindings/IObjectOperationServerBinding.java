package org.essentialplatform.runtime.server.domain.bindings;

import java.util.Map;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectOperationRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IOperationRuntimeBinding;

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