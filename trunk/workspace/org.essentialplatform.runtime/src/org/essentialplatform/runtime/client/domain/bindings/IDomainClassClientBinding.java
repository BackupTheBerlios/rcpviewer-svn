package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;


/**
 * Represents client-specific functionality for a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectClassClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface IDomainClassClientBinding<T> extends IDomainClassRuntimeBinding<T> {
	
}