package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.runtime.shared.domain.bindings.IClassRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectClassRuntimeBinding;


/**
 * Represents server-specific functionality for an <i>instance of</i> a 
 * {@link IDomainClass} (that is, an {@link IDomainObject}).
 * 
 * @author Dan Haywood
 */
public interface IObjectClassServerBinding<T> extends IObjectClassRuntimeBinding<T> {
	
}