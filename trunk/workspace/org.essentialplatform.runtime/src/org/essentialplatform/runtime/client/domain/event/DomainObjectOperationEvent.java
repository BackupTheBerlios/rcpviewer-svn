package org.essentialplatform.runtime.client.domain.event;

import java.util.EventObject;

import org.essentialplatform.runtime.shared.domain.IDomainObject;


/**
 * Event object for events that impact an {@link IDomainObject}.
 * 
 * @author Dan Haywood
 */
public final class DomainObjectOperationEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
 	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public DomainObjectOperationEvent(
			final IDomainObject.IObjectOperation source, 
			final Object[] args) {
		super(source);
		this.args = args;
	}
	
	/**
	 * Type-safe accessor to the source of this event.
	 * 
	 * @return the extended attribute that raised this event.
	 */
	public IDomainObject.IObjectOperation getOperation() {
		return (IDomainObject.IObjectOperation)getSource();
	}


	private final Object[] args;
	public Object[] getArgs() {
		return args;
	}
}
