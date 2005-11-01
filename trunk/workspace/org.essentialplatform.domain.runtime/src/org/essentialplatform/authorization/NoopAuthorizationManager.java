package org.essentialplatform.authorization;

import org.essentialplatform.progmodel.extended.Prerequisites;
import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.standard.IFeatureId;

/**
 * Implementation of {@link AuthorizationManager} that enforces no constraints.
 * 
 * <p>
 * Cannot be accessed or even referenced directly, due to package level 
 * visibility.  Instead, access using {@link IAuthorizationManager#NOOP} 
 * 
 * @author Dan Haywood
 */
final class NoopAuthorizationManager implements IAuthorizationManager {

	/**
	 * Returns a no-op implementation.
	 */
	public final IPrerequisites preconditionsFor(IFeatureId feature) {
		return Prerequisites.none();
	}


}
