package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass.IReference;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectReference;
import org.essentialplatform.runtime.shared.domain.bindings.IReferenceRuntimeBinding;

/**
 * Represents client-specific functionality for an {@link IReference} of 
 * a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectReferenceClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface IReferenceClientBinding extends IReferenceRuntimeBinding, IMemberClientBinding {
	IPrerequisites authorizationPrerequisites();
	IPrerequisites accessorPrerequisitesFor(Object pojo);
}