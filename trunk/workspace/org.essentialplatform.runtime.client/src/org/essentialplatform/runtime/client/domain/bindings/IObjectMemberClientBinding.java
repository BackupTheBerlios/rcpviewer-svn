package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.client.authorization.IAuthorizationManager;
import org.essentialplatform.runtime.client.domain.IObservedFeature;

/**
 * Represents client-specific functionality for an <i>instance of</i> a member 
 * a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectMemberClientBinding extends IObservedFeature {

	/**
	 * Prerequisites applicable to view/edit this attribute according to the
	 * configured {@link IAuthorizationManager}.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @param eAttribute
	 * @return
	 */
	IPrerequisites authorizationPrerequisitesFor();


}