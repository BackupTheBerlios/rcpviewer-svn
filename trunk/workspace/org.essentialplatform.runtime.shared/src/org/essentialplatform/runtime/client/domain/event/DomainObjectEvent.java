package org.essentialplatform.runtime.client.domain.event;

import java.util.EventObject;

import org.essentialplatform.runtime.shared.domain.IDomainObject;


/**
 * Event object for events that impact an {@link IDomainObject}.
 * 
 * @author Dan Haywood
 */
public abstract class DomainObjectEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public DomainObjectEvent(final IDomainObject<?> source) {
		super(source);
	}
	
	/**
	 * Type-safe access to the source of this event.
	 * 
	 * @return the {@link IDomainObject} that raised the event.
	 */
	public IDomainObject<?> getDomainObject() {
		return (IDomainObject<?>)this.getSource();
	}

}
