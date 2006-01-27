package org.essentialplatform.runtime.client.domain.event;

import java.util.EventObject;

import org.essentialplatform.runtime.shared.domain.IDomainObject;


/**
 * Event object for events that impact an {@link IDomainObject}.
 * 
 * <p>
 * Parameterized by the type of the EAttribute that has changed.
 * 
 * @author Dan Haywood
 */
public final class DomainObjectAttributeEvent<T> extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
 	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public DomainObjectAttributeEvent(
			final IDomainObject.IObjectAttribute source, 
			final T newValue) {
		super(source);
		this.newValue = newValue;
	}
	
	/**
	 * Type-safe accessor to the source of this event.
	 * 
	 * @return the extended attribute that raised this event.
	 */
	public IDomainObject.IObjectAttribute getAttribute() {
		return (IDomainObject.IObjectAttribute)getSource();
	}


	private final T newValue;
	public T getNewValue() {
		return newValue;
	}
	
}
