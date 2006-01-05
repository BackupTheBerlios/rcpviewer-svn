package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass.IReference;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectReferenceRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IReferenceRuntimeBinding;

/**
 * Represents server-specific functionality for an <i>instance of</i> an 
 * {@link IReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectReferenceServerBinding 
		extends IObjectReferenceRuntimeBinding, IObjectMemberServerBinding {
}