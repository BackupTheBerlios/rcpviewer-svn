package de.berlios.rcpviewer.authorization;

import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.standard.IFeatureId;

/**
 * Convenience adapter for implementing {@link AuthorizationManager}s.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractAuthorizationManager implements IAuthorizationManager {

	public abstract IPrerequisites preconditionsFor(IFeatureId feature);

}
