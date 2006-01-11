package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.ICollectionReference;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.client.authorization.IAuthorizationManager;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectCollectionReference;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectReference;
import org.essentialplatform.runtime.shared.domain.bindings.ICollectionReferenceRuntimeBinding;

/**
 * Represents client-specific functionality for an {@link ICollectionReference} 
 * of a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectCollectionReferenceClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface ICollectionReferenceClientBinding 
		extends ICollectionReferenceRuntimeBinding, IReferenceClientBinding {

	IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue, boolean beingAdded);
	

}