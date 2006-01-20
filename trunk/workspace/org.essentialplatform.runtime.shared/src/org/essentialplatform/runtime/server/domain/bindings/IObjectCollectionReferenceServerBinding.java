package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.ICollectionReference;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.bindings.ICollectionReferenceRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectCollectionReferenceRuntimeBinding;

/**
 * Represents server-specific functionality for an <i>instance of</i> an 
 * {@link ICollectionReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectCollectionReferenceServerBinding 
		extends IObjectCollectionReferenceRuntimeBinding, IObjectReferenceServerBinding {
}