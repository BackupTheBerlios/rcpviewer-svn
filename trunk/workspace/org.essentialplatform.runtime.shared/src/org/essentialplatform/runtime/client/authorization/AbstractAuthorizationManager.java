package org.essentialplatform.runtime.client.authorization;

import org.essentialplatform.core.features.IFeatureId;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;

/**
 * Convenience adapter for implementing {@link AuthorizationManager}s.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractAuthorizationManager implements IAuthorizationManager {

	public abstract IPrerequisites preconditionsFor(IFeatureId feature);

}
