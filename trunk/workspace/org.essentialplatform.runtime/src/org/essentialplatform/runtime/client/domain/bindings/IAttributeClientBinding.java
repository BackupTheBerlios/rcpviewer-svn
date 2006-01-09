package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute;
import org.essentialplatform.runtime.shared.domain.bindings.IAttributeRuntimeBinding;

/**
 * Represents client-specific functionality for an {@link IAttribute} of 
 * a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectAttributeClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface IAttributeClientBinding extends IAttributeRuntimeBinding, IMemberClientBinding {
	IPrerequisites accessorPrerequisitesFor(Object pojo);
	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue);
	/**
	 * The corresponding instance-level binding.
	 * @param attribute 
	 * @return
	 */
	IObjectAttributeClientBinding getObjectBinding(IObjectAttribute attribute);
}