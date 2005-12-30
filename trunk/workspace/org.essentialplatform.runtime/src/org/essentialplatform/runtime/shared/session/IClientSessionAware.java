package org.essentialplatform.runtime.shared.session;


/**
 * Objects that require an {@link IClientSession} should implement this interface;
 * dependency injection will take care of the rest.
 *
 * <p>
 * TODO: probably replace with an Injected annotation.
 * 
 * @author Dan Haywood
 */

public interface IClientSessionAware {

	void setSession(IClientSession session);
}
