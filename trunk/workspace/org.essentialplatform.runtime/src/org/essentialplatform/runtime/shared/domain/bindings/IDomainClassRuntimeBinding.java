package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.deployment.IDomainClassBinding;
import org.essentialplatform.core.domain.IDomainClass;

/**
 * Represents runtime-specific functionality for a 
 * {@link IDomainClass} (common to both client- and server).
 * 
 * <p>
 * @see IDomainObjectRuntimeBinding. 
 * 
 * @author Dan Haywood
 */
public interface IDomainClassRuntimeBinding<T> extends IDomainClassBinding<T> {
	
}