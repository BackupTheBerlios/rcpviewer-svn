package org.essentialplatform.runtime.shared.session.event;

import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.shared.domain.IDomainObject;


/**
 * Event object for events that relate to an {@link IDomainObject} changing its 
 * state with respect to an {@link IClientSession}.
 * 
 * @author Dan Haywood
 */
public final class SessionObjectEvent<T> extends SessionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * 
	 * @param source
	 */
	public SessionObjectEvent(final IClientSession source, final IDomainObject<T> domainObject) {
		super(source);
	}
	
	public IDomainObject<T> getDomainObject() {
		return (IDomainObject<T>)this.getSource();
	}

}
