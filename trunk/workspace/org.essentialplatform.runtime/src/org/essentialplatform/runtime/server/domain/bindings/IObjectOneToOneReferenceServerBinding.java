package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectOneToOneReferenceRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IOneToOneReferenceRuntimeBinding;

/**
 * Represents server-specific functionality for an <i>instance of</i> an 
 * {@link IOneToOneReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectOneToOneReferenceServerBinding 
		extends IObjectOneToOneReferenceRuntimeBinding, IObjectReferenceServerBinding {
	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue);
}