package org.essentialplatform.runtime.shared.domain.event;

import org.essentialplatform.runtime.shared.domain.IDomainObject;



/**
 * Listeners will be notified of any changes to a specific 
 * {@link IDomainObject}.
 * 
 * @author Dan Haywood
 */
public interface IDomainObjectListener {

	/**
	 * The {@link IDomainObject} has been persisted.
	 * 
	 * @param event
	 */
	public void persisted(DomainObjectEvent event);
}
