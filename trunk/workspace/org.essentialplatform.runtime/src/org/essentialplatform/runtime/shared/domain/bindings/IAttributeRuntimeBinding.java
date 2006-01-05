package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.deployment.IAttributeBinding;

import org.essentialplatform.core.domain.IDomainClass.IAttribute;

/**
 * Represents runtime-specific functionality for an {@link IAttribute} of 
 * a {@link IDomainClass} (common to both client- and server).
 * 
 * <p>
 * @see IObjectAttributeRuntimeBinding. 
 * 
 * @author Dan Haywood
 */
public interface IAttributeRuntimeBinding extends IAttributeBinding, IMemberRuntimeBinding {
	Object invokeAccessor(Object pojo);
	void invokeMutator(Object pojo, Object newValue);
}
