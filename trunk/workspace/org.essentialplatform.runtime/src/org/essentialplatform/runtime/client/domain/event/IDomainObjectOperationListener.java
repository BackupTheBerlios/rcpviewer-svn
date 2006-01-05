package org.essentialplatform.runtime.client.domain.event;

import org.essentialplatform.runtime.shared.domain.IDomainObject;



/**
 * Listeners will be notified of any changes to a specific 
 * {@link IDomainObject.IObjectOperation} of a particular {@link IDomainObject}.
 * 
 * @author Dan Haywood
 */
public interface IDomainObjectOperationListener {

	public void operationInvoked(DomainObjectOperationEvent event);
	
	/**
	 * The prerequisites of an operation of the {@link IDomainObject} have been 
	 * changed.
	 *
	 * <p>
	 * Extended semantics.
	 */
	public void operationPrerequisitesChanged(ExtendedDomainObjectOperationEvent event);


}
