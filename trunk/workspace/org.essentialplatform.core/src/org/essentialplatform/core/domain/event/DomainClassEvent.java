package org.essentialplatform.core.domain.event;

import org.essentialplatform.core.domain.IDomainClass;

/**
 * Event object for events that impact an {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public final class DomainClassEvent extends DomainEvent {

	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;


	/**
 	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public DomainClassEvent(final IDomainClass domainClass) {
		super(domainClass.getDomain());
		this.domainClass = domainClass;
	}
	
	private final IDomainClass domainClass;
	/**
	 * The {@link IDomainClass} upon which the event has occurred.
	 * 
	 * @return
	 */
	public IDomainClass getDomainClass() {
		return domainClass;
	}
	
}
