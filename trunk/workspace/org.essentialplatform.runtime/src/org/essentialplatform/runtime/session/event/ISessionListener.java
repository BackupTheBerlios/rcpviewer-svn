package org.essentialplatform.runtime.session.event;

import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.session.ISession;

/**
 * Listeners will be notified of any changes to an {@link ISession}.
 * 
 * @author Dan Haywood
 */
public interface ISessionListener {

	/**
	 * An {@link org.essentialplatform.runtime.domain.IDomainObject} has been 
	 * attached to the session.
	 * 
	 */
	public void domainObjectAttached(SessionObjectEvent event);

	/**
	 * An {@link org.essentialplatform.runtime.domain.IDomainObject} has been 
	 * dettached to the session.
	 * 
	 */
	public void domainObjectDetached(SessionObjectEvent event);

	
	
}
