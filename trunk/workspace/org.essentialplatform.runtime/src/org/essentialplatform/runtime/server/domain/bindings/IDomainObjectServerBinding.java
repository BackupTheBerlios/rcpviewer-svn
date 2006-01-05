package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.runtime.shared.domain.bindings.IDomainObjectRuntimeBinding;


/**
 * Represents server-specific functionality for an <i>instance of</i> a 
 * {@link IDomainClass} (that is, an {@link IDomainObject}).
 * 
 * @author Dan Haywood
 */
public interface IDomainObjectServerBinding<T> extends IDomainObjectRuntimeBinding<T> {
	
}