/**
 * 
 */
package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.handle.IHandleAssigner;

/**
 * Represents server-specific functionality for a {@link IDomainClass}.
 * 
 * <p>
 * @see IDomainObjectClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface IDomainClassServerBinding<T> extends IDomainClassRuntimeBinding<T>, IHandleAssigner {
	
}