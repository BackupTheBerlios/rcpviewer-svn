package org.essentialplatform.session;


/**
 * Objects that require an {@link ISession} should implement this interface;
 * dependency injection will take care of the rest.
 *
 * <p>
 * TODO: probably replace with an Injected annotation.
 * 
 * @author Dan Haywood
 */

public interface ISessionAware {

	void setSession(ISession session);
}
