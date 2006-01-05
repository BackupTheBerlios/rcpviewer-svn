package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.runtime.shared.domain.bindings.IClassRuntimeBinding;


/**
 * Represents client-specific functionality for a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectClassClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface IClassClientBinding<T> extends IClassRuntimeBinding<T> {
	
}