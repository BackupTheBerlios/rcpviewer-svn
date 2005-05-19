package de.berlios.rcpviewer.domain;

import java.util.EventObject;

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
	DomainEvent(final Domain source) {
		super(source);
	}
	
	/**
	 * Type-safe access to the source of this event.
	 * 
	 * @return the metamodel that raised the event.
	 */
	public Domain getDomain() {
		return (Domain)this.getSource();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
