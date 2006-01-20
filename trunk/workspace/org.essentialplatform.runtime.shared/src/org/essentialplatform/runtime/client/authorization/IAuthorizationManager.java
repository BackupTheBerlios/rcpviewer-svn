package org.essentialplatform.runtime.client.authorization;

import org.essentialplatform.core.features.IFeatureId;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.IDomainObject;

/**
 * Facade for determining whether the current user can see and if so can use
 * the attributes and operations of domain objects.
 * 
 * <p>
 * It wouldn't be wise to embed security-related information into the domain
 * objects themselves because different deployments would partition the
 * security authorizations in different ways.  So instead we arrange it such
 * that the {@link IDomainObject} interacts with the pojo but also with this
 * authorization manager, providing a {@link IFeatureId} which represents - erm -
 * the feature that the user is (attempting to) interact with.
 * 
 * <p>
 * In this way the authorization manager is able to veto access either by
 * making the feature unusable (not editable for attribute, not invokable for
 * an operation) or indeed even invisible.
 * 
 * @author Dan Haywood
 */
public interface IAuthorizationManager {

	/**
	 * Implementation of {@link AuthorizationManager} that enforces no 
	 * constraints.
	 * 
	 * <p>
	 * Useful for testing before an authorization manager proper has been 
	 * implemented.
	 * 
	 * @author Dan Haywood
	 */
	IAuthorizationManager NOOP = new NoopAuthorizationManager();

	/**
	 * Returns a {@link IPrerequisites} that represents the visibility and
	 * usability (editable for attributes, invokability for operations) of the
	 * specified {@link IFeatureId} for the current user.
	 * 
	 * <p>
	 * It is up to the implementation to figure out who the current user is
	 * (though implementations are expected to interact with some sort of
	 * authentication manager that holds this information).
	 * 
	 * @param feature
	 * @return
	 */
	public IPrerequisites preconditionsFor(final IFeatureId feature);
}
