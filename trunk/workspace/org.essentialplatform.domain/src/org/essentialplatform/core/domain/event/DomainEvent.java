package org.essentialplatform.core.domain.event;

import java.util.EventObject;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;

/**
 * Event object for events that impact an {@link Domain}.
 * 
 * @author Dan Haywood
 */
public class DomainEvent extends EventObject {

	/**
	 * Package local visibility so that the only subclasses must be in this
	 * package.
	 * 
	 * @param source
	 */
	DomainEvent(final IDomain source) {
		super(source);
	}
	
	/**
	 * Type-safe access to the source of this event.
	 * 
	 * @return the metamodel that raised the event.
	 */
	public IDomain getDomain() {
		return (IDomain)this.getSource();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
