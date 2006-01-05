/**
 * 
 */
package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.runtime.server.persistence.IPersistenceIdAssigner;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;

/**
 * Represents server-specific functionality for a {@link IDomainClass}.
 * 
 * <p>
 * @see IDomainObjectClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface IDomainClassServerBinding<T> extends IDomainClassRuntimeBinding<T>, IPersistenceIdAssigner {
	
}