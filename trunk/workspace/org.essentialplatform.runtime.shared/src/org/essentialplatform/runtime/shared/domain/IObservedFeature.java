package org.essentialplatform.runtime.shared.domain;

/**
 * A feature (attribute, reference or operation) of some domain object that is
 * currently being observed in the UI.
 * 
 * <p>
 * On the client, the <tt>IClientSession</tt> holds a weak hash map of all 
 * observed features so that if there is a state change to some domain object 
 * (either by this user or perhaps if notified of a change by some other user 
 * through the distribution mechanism) then all <tt>IPrerequisites</tt> can be 
 * recomputed.  If there is a change in their result then this can be notified.
 * 
 * <p>
 * Note that this is in the shared plugin because AbstractChange depends upon 
 * it. 
 * 
 * 
 * @author Dan Haywood
 */
public interface IObservedFeature {
	
	/**
	 * Notifies this feature that some state (external to this feature) has
	 * changed so that this feature may be stale.
	 *
	 * <p>
	 * Implementations should notify their respective observers if they have
	 * changed, in particular their {@link IPrerequisites}.
	 */
	public void externalStateChanged();
}
