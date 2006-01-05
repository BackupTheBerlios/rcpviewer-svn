package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IOneToOneReference;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.bindings.IOneToOneReferenceRuntimeBinding;

/**
 * Represents client-specific functionality for an {@link IOneToOneReference} of 
 * a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectOneToOneReferenceClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface IOneToOneReferenceClientBinding 
		extends IOneToOneReferenceRuntimeBinding, IReferenceClientBinding {
	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue);
}