package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.deployment.IClassBinding;
import org.essentialplatform.runtime.server.persistence.IPersistenceIdAssigner;

/**
 * Represents runtime-specific functionality for a 
 * {@link IDomainClass} (common to both client- and server).
 * 
 * <p>
 * @see IDomainObjectRuntimeBinding. 
 * 
 * @author Dan Haywood
 */
public interface IClassRuntimeBinding<T> extends IClassBinding<T> {
	
}