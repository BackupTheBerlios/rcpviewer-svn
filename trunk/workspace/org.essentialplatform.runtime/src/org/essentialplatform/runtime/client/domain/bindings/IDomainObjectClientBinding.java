package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainObjectRuntimeBinding;


/**
 * Represents client-specific functionality for an <i>instance of</i> a 
 * {@link IDomainClass} (that is, an {@link IDomainObject}).
 * 
 * @author Dan Haywood
 */
public interface IDomainObjectClientBinding<T> extends IDomainObjectRuntimeBinding<T> {
	
}