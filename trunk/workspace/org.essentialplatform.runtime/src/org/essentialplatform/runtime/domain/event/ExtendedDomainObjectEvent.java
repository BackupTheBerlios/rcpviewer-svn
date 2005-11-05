package org.essentialplatform.runtime.domain.event;

import java.util.EventObject;

import org.essentialplatform.runtime.domain.IDomainObject;



/**
 * Event object for events that impact an {@link IExtendedDomainObject}.
 * 
 * @author Dan Haywood
 */
public abstract class ExtendedDomainObjectEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public ExtendedDomainObjectEvent(final IDomainObject<?> source) {
		super(source);
	}
	
	/**
	 * Type-safe access to the source of this event.
	 * 
	 * @return the {@link IExtendedDomainObject} that raised the event.
	 */
	public IDomainObject<?> getExtendedDomainObject() {
		return (IDomainObject<?>)this.getSource();
	}

}
