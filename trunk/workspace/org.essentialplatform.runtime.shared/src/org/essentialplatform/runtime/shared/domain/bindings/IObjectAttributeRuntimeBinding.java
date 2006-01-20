package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;

/**
 * Represents runtime-specific functionality (common to client and server) for 
 * an <i>instance of</i> a {@link IAttribute} of a {@link IDomainClass}
 * 
 * @author Dan Haywood
 */
public interface IObjectAttributeRuntimeBinding extends IObjectMemberRuntimeBinding {

	/**
	 * Invoked after {@link IDomainObject#getAttribute(IDomainClass.IAttribute)}
	 * so that the binding can perform any further processing.
	 * 
	 * <p>
	 * For example, on the client, can add the attribute to the list of
	 * observed features.
	 */
	void gotAttribute();
}