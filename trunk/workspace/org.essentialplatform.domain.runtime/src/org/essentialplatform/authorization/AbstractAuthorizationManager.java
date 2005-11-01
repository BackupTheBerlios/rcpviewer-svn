package org.essentialplatform.authorization;

import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.standard.IFeatureId;

/**
 * Convenience adapter for implementing {@link AuthorizationManager}s.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractAuthorizationManager implements IAuthorizationManager {

	public abstract IPrerequisites preconditionsFor(IFeatureId feature);

}
