package de.berlios.rcpviewer.domain.runtime;

import de.berlios.rcpviewer.authorization.IAuthorizationManager;
import de.berlios.rcpviewer.domain.IDomain;

/**
 * Originally introduced as a place to (dependency) inject system-level 
 * components for a domain that are only meaningful at runtime.
 * 
 * <p>
 * For example, authorization manager.
 * @author Dan Haywood
 *
 */
public interface IRuntimeDomain extends IDomain {

	/**
	 * Dependency injection of authorization manager.
	 * 
	 * @param authorizationManger
	 */
	void setAuthorizationManager(IAuthorizationManager authorizationManger);

	/**
	 * Injected authorization manager (if any) for the domain.
	 * 
	 * <p>
	 * If no authorization manager has been injected then will return null.
	 * 
	 * @return
	 */
	IAuthorizationManager getAuthorizationManager();

}
